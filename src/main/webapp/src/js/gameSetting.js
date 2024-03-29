function Setting() {
    time = 0;
    score = 0;
    combo = 0;
    maxCombo = 0;
}

Setting.prototype.change = function() {
    starttime = 3;
    $('.setting-nav').css('visibility', 'hidden');
    $('.start').show();
}