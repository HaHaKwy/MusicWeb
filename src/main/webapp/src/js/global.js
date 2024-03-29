function messageBox(msg,tp){
    // create the notification
    var notification = new NotificationFx({
        message: msg,
        layout: 'growl',
        effect: 'slide',
        type: tp // notice, warning or error
    });
    // show the notification
    notification.show();
}

let IP_ADDR = "172.24.69.162";