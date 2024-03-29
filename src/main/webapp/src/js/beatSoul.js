let halfTime = 0;
$(function() {
    let tap = null;
    let start = null;
    let Time = null;
    let startTime = null;
    media = $("#Au").get(0);

//    $(media).on("timeupdate",function(){
//        console.log("update");
//
//        let curTime = parseFloat(media.currentTime.toFixed(2))+4.00;
//        console.log(media.currentTime.toFixed(2));
//        console.log(Math.abs(curTime - tapRule[line][0]));
//
//        while(Math.abs(curTime - tapRule[line][0]) < 0.5){
//            tap = new Tap(tapRule[line][1]);
//            line ++;
//        }
//    });

    /* 监听开始按钮 */
    $('.start').on('click', function() {
        $(this).stop().animate({
            opacity: 0
        }, 600, 'linear', function() {
            $('.setting-wrap').hide();
            $('.start').hide();
            startGame();
        });
        $('.score').text('0');
        $('.combo').text('0');
        $('.maxCombo').text('0');
        new Setting().change();
    });

    $('.stop-game').on('click', function() {
        endGame();
    });

    /* 监听按键 */
    $(document).on('keydown', function(e) {
        if (tap) {
            if(e.keyCode == 27){
                endGame();
            }
            else{
                screenCheck(e.keyCode);
            }
        }
        else if(e.keyCode == 13){
            $('.start').click();
        }
    });

    function startGame() {
        $('.countdown').show();
        $('.countdown').css("color","white");
        $('.countdown').text(starttime);
        $('.countdown').fadeOut();
        startTime = setInterval(function() {
            starttime--;
            if(starttime == 0){
                $('.countdown').css("color","white");
                $('.countdown').text("START");
                $('.countdown').show();
            }
            else{
                $('.countdown').css("color","white");
                $('.countdown').text(starttime);
                $('.countdown').show();
                $('.countdown').fadeOut();
            }
            if (starttime == -1) {
                $('.countdown').hide();
                $('.stop-game').show();
                clearInterval(startTime);
            }
        }, 1000);

        showTime();
        start = setTimeout(function() {
            line = 0;
            media.currentTime = 0;
            media.play();
            time = 0;
            halfTime = 0;
            beater = setInterval(function(){
                halfTime += 0.5;
                if(Math.abs(halfTime - tapRule[line][0]) < 0.5){
                    tap = new Tap(tapRule[line][1]);
                    line ++;
                }
            },500);
            Time = setInterval(function() {
                time++;
                if (time == valTime) {
                    endGame();
                }
                showTime();
            }, 1000);
        }, 3000);
    };

    function screenCheck(keyCode) {
        if (keyCode == 68) {
            tap.tapClean($('#row-one'));
            screenAnimate($('#screen-one .screen-outline'));
        }else if (keyCode == 70) {
            tap.tapClean($('#row-two'));
            screenAnimate($('#screen-two .screen-outline'));
        }else if (keyCode == 74) {
            tap.tapClean($('#row-three'));
            screenAnimate($('#screen-three .screen-outline'));
        }else if (keyCode == 75) {
            tap.tapClean($('#row-four'));
            screenAnimate($('#screen-four .screen-outline'));
        }
    }

    function screenAnimate(obj) {
        obj.stop();
        obj.animate({
            width: "80px",
            height: "80px",
            top: "-18px",
            left: "-18px",
            opacity: 1.0
        }, 0, 'swing', function(){
            obj.animate({
                width: "60px",
                height: "60px",
                top: "-8px",
                left: "-8px",
                opacity: 0
            }, 500, 'linear');
            obj.stop();
        });
    }

    function endGame() {
        $('.stop-game').animate({
            opacity: 0
        }, 600, 'linear', function() {
            $('.stop-game').hide();
            $('.stop-game').css('opacity', '1');
        });
        clearInterval(beater);
        clearTimeout(start);
        clearInterval(Time);
        media.pause();
        $('.start').css('opacity', '1');
        roadCheck();
    }
    function roadCheck() {
        if ($('.row').children().length == 0) {
            $('.start').delay('500').show(0);
            $('.setting-wrap').delay('500').show(0);
            $('#endTime-minute').text('0');
            $('#endTime-second').text('00');
            tap = null;
            return;
        }else {
            let endtime = setInterval(function() {
                roadCheck();
                clearInterval(endtime);
            }, 500);
        }
    }
    function showTime() {
        let endTime = valTime - time;
        let endTime_minute = Math.floor(endTime/60);
        let endTime_second = endTime - (60*endTime_minute);
        endTime_second = endTime_second<10 ? '0' + endTime_second : endTime_second;
        $('#endTime-minute').text(endTime_minute);
        $('#endTime-second').text(endTime_second);
    }

    function randomNum (max, min) {
        let num = Math.floor(Math.random() * (max - min + 1) + min);
        return num;
    }
});