var Event = require('../models/event.js');
var User = require('../models/user.js');

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function getGUID() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

exports.createEvent = function (req, res) {
    var newEvent = new Event();
    newEvent.id = getGUID();
    newEvent.hostID = req.params.hostID;
    newEvent.name = req.params.name;
    newEvent.save();
    res.json(newEvent.id);
};

exports.subscribeUser = function (req, res) {
    Event.findOne({'id': req.params.id}, function (err, id) {
        if (!id) {
            res.json('Try to fool me, this event does not exist.');
        } else {
            if (id.uniqueUsers.indexOf(req.params.userID) != -1) {
                id.uniqueUsers.push(req.params.userID);
                id.save();
            }
            res.end();
        }
    });
};

exports.addTicketsToUser = function (req, res) {
    Event.findOne({'id': req.params.id}, function (err, id) {
        if (!id) {
            res.json('Try to fool me, this event does not exist.');
        } else {
            if (id.uniqueUsers.indexOf(req.params.userID) == -1) {
                exports.subscribeUser(req, res);
            }
            for (var i = 0; i < parseInt(req.params.count); ++i) {
                id.tickets.push(req.params.userID);
            }
            id.save();
            res.end();
        }
    });
};

exports.getUsers = function (req, res) {
    Event.findOne({'id': req.params.id}, function (err, id) {
        if (!id) {
            res.json('Try to fool me, this event does not exist.');
        } else {
            res.json(id.uniqueUsers);
        }
    });
};

exports.getWinner = function (req, res) {
    Event.findOne({'id': req.params.id}, function (err, id) {
        if (!id) {
            res.json('Try to fool me, this event does not exist.');
        } else {
            var winnerIndex = getRandomInt(0, id.tickets.length - 1);
            var winnerID = id.tickets[winnerIndex];
            User.findOne({'id': winnerID}, function (err, user) {
                res.json(user);
            });
        }
    });
};

exports.getName = function (req, res) {
    Event.findOne({'id': req.params.id}, function (err, id) {
        if (!id) {
            res.json('Try to fool me, this event does not exist.');
        } else {
            res.json(id.name);
        }
    });
};

exports.removeTickets = function (req, res) {
    Event.findOne({'id': req.params.id}, function (err, id) {
        if (!id) {
            res.json('Try to fool me, this event does not exist.');
        } else {
            while (id.tickets.length > 0) {
                id.tickets.pop();
                id.save();
            }
        }
        res.end();
    });
};