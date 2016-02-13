var mongoose = require('mongoose');

module.exports = mongoose.model('Event', {
    id: String,
    hostID: String,
    name: String,
    tickets: [String],
    uniqueUsers: [String],
    winnerID: String,
    contestStarted: Boolean,
    canBuyTickets: Boolean,
    price: Number
});
