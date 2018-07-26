// 存放主要交互逻辑js代码
// JavaScript 模块化
var seckill = {
    // 封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        },
        executiontest: function (seckillId, md5, phone) {
            return '/seckill/' + seckillId + '/' + md5 + '/' + phone + '/execution';
        }
    },
    getRandomNumber: function (n) {
        var rnd = "";
        for (var i = 0; i < n; i++)
            rnd += Math.floor(Math.random() * 10);
        return rnd;
    },
    handleSeckillKill: function (seckillId, node) {
        // 处理秒杀逻辑
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开 始 秒 杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            // 在回调函数中，执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    // 活动开启
                    // 获取地址
                    var md5 = exposer['md5'];
                    // var killUrl = seckill.URL.execution(seckillId, md5);
                    var phone = seckill.getRandomNumber(3) + '' + seckill.getRandomNumber(3) + '' + seckill.getRandomNumber(4);
                    var killUrl = seckill.URL.executiontest(seckillId, md5, phone);
                    console.log("killUrl: " + killUrl);
                    // 绑定秒杀按钮事件,只绑定
                    $('#killBtn').one('click', function () {
                        // 执行秒杀请求
                        // 先禁用按钮
                        $(this).addClass('disabled');
                        // 发送秒杀请求请求执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                // 显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>')
                            }
                        })
                    });
                    node.show();
                } else {
                    // 未开启
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    // 为防止客户机时间偏差，重新计算计时逻辑
                    seckill.countDOwn(seckillId, now, start, end);
                }
            } else {
                console.log("result: " + result);
            }
        })
    },
    countDOwn: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        console.log(nowTime + " " + startTime + " " + endTime);
        // 时间判断
        if (nowTime > endTime) {
            // 秒杀结束
            seckillBox.html('秒杀结束！');
        } else if (nowTime < startTime) {
            // 秒杀未开始,计时时间绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                // 获取秒杀地址，控制逻辑，执行秒杀
                seckill.handleSeckillKill(seckillId, seckillBox);

            });
        } else {
            // 秒杀开始
            seckill.handleSeckillKill(seckillId, seckillBox);
        }
    },
    // 验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    // 详情页秒杀逻辑
    detail: {
        // 详情页初始化
        init: function (params) {
            // 手机验证和登录，计时交互
            // 规划交互流程
            // 在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            // var killPhone = seckill.getRandomNumber(3) + '' + seckill.getRandomNumber(3) + '' + seckill.getRandomNumber(4);
            // $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});// cookie有效期7天

            // 验证手机号
            if (!seckill.validatePhone(killPhone)) {
                // 绑定phone
                // 控制输出
                var killPhoneModal = $('#killPhoneModal');
                // 显示弹出层
                killPhoneModal.modal({
                    show: true,  // 显示弹出层
                    backdrop: 'static',  // 禁止位置关闭
                    keyboard: false  // 关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    alert(inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        // 电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});// cookie有效期7天
                        window.location.reload();
                    } else {
                        // 错误提示信息“手机号错误”不应该直接写死，应该有一个统一的前端字典
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);// 延迟300毫秒后显示
                    }
                });
            }
            // 已经登录
            // 计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    // 时间判断
                    seckill.countDOwn(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result: ' + result);
                }
            });
        }
    }
};