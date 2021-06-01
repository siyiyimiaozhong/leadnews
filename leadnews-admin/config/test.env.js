'use strict'
const merge = require('webpack-merge')
const devEnv = require('./dev.env')

module.exports = merge(devEnv, {
  NODE_ENV: '"testing"',
  BASEURL:"'/server_85/ADMIN'",
  WS_BASEURL:"'/server_85/WEBSOCKET'",
  WS_SERVER:"'localhost:9009'"
})
