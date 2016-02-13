var express = require('express');
var router = express.Router();
var eventCalls = require('../calls/event_calls.js');
var userCalls = require('../calls/user_calls.js');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/users/:id', userCalls.getUser);
router.get('/users/create/:id/:firstName/:lastName/:picURL', userCalls.createUser);
router.get('/users/wipe/all', userCalls.wipeAll);

router.get('/events', eventCalls.getAllEvents);
router.get('/events/create/:hostID/:name/:price', eventCalls.createEvent);
router.get('/events/:id/users/', eventCalls.getUsers);
router.get('/events/:id/tickets/:userID/:count', eventCalls.addTicketsToUser);
router.get('/events/:id/winner', eventCalls.getWinner);
router.get('/events/:id/wipetickets', eventCalls.removeTickets);
router.get('/events/:id/name', eventCalls.getName);
router.get('/events/:id/conteststarted', eventCalls.isContestStarted);
router.get('/events/:id/setconteststarted/:contestStarted', eventCalls.setContestStarted);
router.get('/events/:id/canbuytickets', eventCalls.canBuyTickets);
router.get('/events/:id/setcanbuytickets/:canBuyTickets', eventCalls.setCanBuyTickets);

module.exports = router;
