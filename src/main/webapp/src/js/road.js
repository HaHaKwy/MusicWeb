let score = 0;
let combo = 0;
let maxCombo = 0;
function Tap(row) {
    this.createTap(row);
}

Tap.prototype.createTap = function(row) {
    let tap = "<div class=\"tap\"></div>";
    if (row == 1) {
        $('#row-one').append(tap);
        this.tapAnimate($('#row-one .tap:last-child'));
    }else if (row == 2) {
        $('#row-two').append(tap);
        this.tapAnimate($('#row-two .tap:last-child'));
    }else if (row == 3) {
        $('#row-three').append(tap);
        this.tapAnimate($('#row-three .tap:last-child'));
    }else if (row == 4) {
        $('#row-four').append(tap);
        this.tapAnimate($('#row-four .tap:last-child'));
    }
}

Tap.prototype.tapAnimate = function(obj) {
    let time = (105-obj.position().top) * dificultSpeed;
    obj.stop().animate({
        top: '100%',
        content: 'MISS'
    }, time, 'linear', function() {
        combo = 0;
        $('.combo').text(combo);
        $('.countdown').css("color","grey");
        $('.countdown').text("MISS");
        $('.countdown').show();
        $('.countdown').fadeOut();
        obj.remove();
    });
}

Tap.prototype.tapClean = function(obj, reallyClean) {
    if (reallyClean) {
        let top = obj.attr('style');
        top = top.split(':')[1].split('%')[0];
        if(top<=96){
            score += 200;
            $('.score').text(score);
            $('.countdown').css("color","gold");
            $('.countdown').text("PERFECT");
            $('.countdown').show();
            $('.countdown').fadeOut();
        }else{
            score += 100;
            $('.score').text(score);
            $('.countdown').css("color","pink");
            $('.countdown').text("GREAT");
            $('.countdown').show();
            $('.countdown').fadeOut();
        }
        combo++;
        $('.combo').text(combo);
        if(combo > maxCombo){
            maxCombo = combo;
            $('.maxCombo').text(maxCombo);
        }
        let cob = $('.combo');
        cob.stop();
        cob.animate({
            'font-size': "90px"
        }, 0, 'swing', function(){
            cob.animate({
                'font-size': "50px"
            }, 500, 'linear');
            cob.stop();
        });
        obj.css("background","green");
        this.tapRemove(obj);
    }else {
        let tap = new Array();
        obj.children().each(function(i, obj) {
            tap.push($(this));
        });
        for (i in tap) {
            this.tapCheck(tap[i]);
        }
    }
}

Tap.prototype.tapRemove = function(obj) {
    obj.stop().animate({
        opacity: '0'
    }, 100, 'linear', function() {
       obj.remove();
    });
}

Tap.prototype.tapCheck = function(obj) {
    let top = obj.attr('style');
    top = top.split(':')[1].split('%')[0];
    if (top >= 94.5 && top <= 99) {
        this.tapClean(obj, true);
    }
}