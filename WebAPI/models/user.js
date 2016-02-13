var mongoose = require('mongoose');

module.exports = mongoose.model('user', {
    id: String,
    firstName: String,
    lastName: String,
    picURL: String
})
