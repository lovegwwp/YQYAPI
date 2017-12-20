/**
 * Created by Administrator on 2017/12/19 0019.
 */

var ajaxStr='http://192.168.0.28:8080/';
var sessionId;
$('.check_read').click(function () {
   var src=$(this).find('img').attr('src');
   if(src=='img/n-a.png'){
       $(this).find('img').attr('src','img/a.png');
       $(this).addClass('active')
   }else {
       $(this).find('img').attr('src','img/n-a.png');
       $(this).removeClass('active');
   }
});
$('.getCodeBtn').click(function () {
     if($('.telephone').val()==''){
         alert('请输入手机号')
     }else if($('.telephone').val().length!=11){
         alert('请输入正确的手机号')
     }else {
        $(this).attr('disabled',true);
        var tel=$('.telephone').val();
        sendMessage(tel);
        $(this).html('<span class="count_down">60</span><span class="count_down_text">S后重新获取</span>')
        var timer=setInterval(function () {
            var time=parseInt($('.count_down').html());
            time--;
            console.log(time);
            $('.count_down').html(time);
            if(time==0){
                console.log('结束');
                clearInterval(timer);
                $('.getCodeBtn').html('<span class="getCode_text">点击获取验证码</span>').attr('disabled',false)
            }
        },1000)
     }
});

$('.registerBtn').click(function () {
    var flog1=false;   //手机
    var flog2=false;   //验证码
    var flog3=false;   //密码
    var flog4=false;   //密码相同确定
    var flog5=false;   //阅读注册协议
    var tel= $('.telephone').val();     //电话
    var password= $('.password').val(); //密码
    var repassword= $('.re_password').val();
    var code= $('.message').val();  //验证码
    //电话验证
    if(tel==''){
        $('.remind').show().find('.remind_text').html('手机号不能为空')
        return;
    }else if(tel.length!=11){
        $('.remind').show().find('.remind_text').html('请输入正确的手机号');
        return;
    }else {
        flog1=true;
    }
    //验证码验证
    if(code==''){
        $('.remind').show().find('.remind_text').html('验证码不能为空');
        return;
    }else {
        flog2=true;
    }

    //密码长度验证
    if(password==''||repassword==''){
        $('.remind').show().find('.remind_text').html('密码不能为空');
    }else if(password.length<6||repassword.length<6){
        $('.remind').show().find('.remind_text').html('密码长度需要6位及以上');
        return;
    }else {
        flog3=true;
    }
    // 密码相同验证
    if(password!=repassword){
        $('.remind').show().find('.remind_text').html('两次输入密码不同');
        return;
    }else{
        flog4=true;
    }
    // 阅读注册协议验证
    if($('.check_read.active').length==1){
        flog5=true;
    }else{
        $('.remind').show().find('.remind_text').html('请认真阅读注册协议并确认');
        return;
    }
    if(flog1&&flog2&&flog3&&flog4&&flog5){
        signOn(tel,password,code);
    }
});

function sendMessage(tel) {
    $.ajax({
        url: ajaxStr + 'YQYAPI/b/sendCode.action',
        data: {
            tel:tel,
            bz:1
        },
        type: "POST",
        dataType: "json",
        success: function (data) {
            console.log(data);
            sessionId=data.data.sessionId;
            console.log(data.status);
            console.log(data.message);
        },
        err: function (err) {
            console.log(err);
        }
    })
}
function signOn(tel,password,code) {
    $.ajax({
        url: ajaxStr + 'YQYAPI/b/signOn.action',
        data:{
            account:tel,
            password:password,
            code:code,
            sessionId:sessionId
        },
        type: "POST",
        dataType: "json",
        success: function (data) {
            console.log(data);
            console.log(data.status);
            if(data.status=='true'){
                $('.remind').show().find('.remind_text').html('注册成功');
                $('box').append(' <div class="ok_register"> <p class="ok_register_text">你已经成功注册易起云</p> <p class="ok_register_text">快去体验吧！</p> <button class="ok_register_ok">打开易起云</button> </div>')
                var ele=document.getElementsByClassName('ok_register_ok')[0];
                ele.onclick=function () {
                    $('.ok_register').hide();
                }
            }else {
                $('.remind').show().find('.remind_text').html('注册失败')
            }
            console.log(data.message);
        },
        err: function (err) {
            console.log(err);
        }
    })
}

$('.remind_ok').click(function () {
   $('.remind').hide();
});