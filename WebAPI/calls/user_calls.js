var User = require('../models/user.js');

exports.createUser = function(req, res){
    User.findOne({'id': req.params.id}, function(err, id){
        if(id) {
            res.json('Disastrously, this user already exists.');
        }else{
            var newUser = new User();
            newUser.id = req.params.id;
            newUser.firstName = req.params.firstName;
            newUser.lastName = req.params.lastName;
            newUser.picURL = req.params.picURL;

            newUser.save();
            res.end();
        }
    });

};

exports.getUser = function(req, res){
    User.findOne({'id' : req.params.id}, function(err, id){
        if(!id)
            res.json('If there\'s a nuclear winter, at least it\'ll snow.');
        else
            res.json(id);
    });
};

exports.wipeAll = function(req, res){
    User.collection.remove();
    res.end();
};