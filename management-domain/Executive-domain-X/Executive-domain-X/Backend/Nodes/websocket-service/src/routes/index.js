const express = require('express');
const router = express.Router();
const healthController = require('../controllers/HealthController');
const broadcastController = require('../controllers/BroadcastController');
const roomController = require('../controllers/RoomController');
const presenceController = require('../controllers/PresenceController');

const apiPrefix = (req, res, next) => {
  req.apiVersion = 'v1';
  next();
};

router.use(apiPrefix);

router.get('/health', healthController.getHealth.bind(healthController));
router.get('/health/ready', healthController.getReady.bind(healthController));
router.get('/health/live', healthController.getLive.bind(healthController));
router.get('/metrics', healthController.getMetrics.bind(healthController));
router.get('/connections', healthController.getConnections.bind(healthController));

router.post('/broadcast/room', broadcastController.broadcastToRoom.bind(broadcastController));
router.post('/broadcast/user', broadcastController.broadcastToUser.bind(broadcastController));
router.post('/broadcast/tenant', broadcastController.broadcastToTenant.bind(broadcastController));
router.post('/broadcast/global', broadcastController.broadcastGlobal.bind(broadcastController));
router.get('/broadcast/stats', broadcastController.getBroadcastStats.bind(broadcastController));

router.post('/broadcast/kpi', broadcastController.sendKpiUpdate.bind(broadcastController));
router.post('/broadcast/dashboard', broadcastController.sendDashboardUpdate.bind(broadcastController));
router.post('/broadcast/notification', broadcastController.sendNotification.bind(broadcastController));
router.get('/broadcast/room/:roomId/history', broadcastController.getRoomHistory.bind(broadcastController));
router.delete('/broadcast/room/:roomId/history', broadcastController.clearRoomHistory.bind(broadcastController));

router.post('/rooms', roomController.createRoom.bind(roomController));
router.get('/rooms', roomController.listRooms.bind(roomController));
router.get('/rooms/:roomId', roomController.getRoom.bind(roomController));
router.put('/rooms/:roomId', roomController.updateRoom.bind(roomController));
router.delete('/rooms/:roomId', roomController.deleteRoom.bind(roomController));
router.get('/rooms/:roomId/users', roomController.getRoomUsers.bind(roomController));
router.get('/rooms/:roomId/presence', roomController.getRoomPresence.bind(roomController));
router.get('/users/:userId/rooms/:tenantId', roomController.getUserRooms.bind(roomController));

router.get('/presence/:tenantId/:userId', presenceController.getPresence.bind(presenceController));
router.get('/presence/:tenantId/users/online', presenceController.getOnlineUsers.bind(presenceController));
router.get('/presence/:tenantId/users/count', presenceController.getOnlineCount.bind(presenceController));
router.get('/presence/:tenantId/stats', presenceController.getPresenceStats.bind(presenceController));
router.get('/presence/:tenantId/:userId/online', presenceController.isUserOnline.bind(presenceController));
router.put('/presence/:tenantId/status', presenceController.updateStatus.bind(presenceController));

module.exports = router;
