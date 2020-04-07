<template>
  <div>
    <Breadcrumb>
      <BreadcrumbItem to="/home">
        <Icon type="ios-home" class="icon-top-1"></Icon> 动态配置中心
      </BreadcrumbItem>
    </Breadcrumb>

    <Row style="margin-top: 12px" class="help">
      <Tag type="border">
        <div style="display: flex; flex-flow: column;">
          <div style="flex: 1"></div>
          <div>动态配置中心（JSwitch）是提供给业务方的的一套平台+SDK的配置管理解决方案，包含的功能有配置管理、版本管理、多环境支持、权限管理等。<a>使用帮助</a></div>
          <div style="flex: 1"></div>
        </div>
      </Tag>
    </Row>
    <Row style="margin-top: 12px">
      <Col span="24">
        <Tabs value="favorite-data">
          <TabPane label="数据看板" name="favorite-data">
              <Row>
                <Col span="6">
                  <div style="margin:5px; border-left: 4px solid rgb(25, 190, 107); height: 100px; cursor: pointer">
                    <Row>
                      <Col span="6">
                        <div style="line-height: 100px; text-align: center;">
                          <Icon type="md-desktop" size="30" color="#19be6b"/>
                        </div>
                      </Col>
                      <Col span="18">
                        <div style="padding: 0px 5px; font-size: 16px; color: #7B7B7B;">
                          在线机器
                        </div>
                        <div style="padding: 5px; font-size: 28px; font-weight: 500; color: #000;">
                          {{nodeCount}}
                        </div>
                        <div style="padding: 0px 5px; font-size: 14px; color: #7B7B7B;">
                          机器总数
                        </div>
                      </Col>
                    </Row>
                  </div>
                </Col>
                <Col span="6">
                  <router-link :to="{name: 'Apps', params: {}}">
                    <div style="margin:5px; border-left: 4px solid rgb(45, 183, 245); height: 100px; cursor: pointer">
                      <Row>
                        <Col span="6">
                          <div style="line-height: 100px; text-align: center;">
                            <Icon type="ios-apps-outline" size="30" color="#2db7f5"/>
                          </div>
                        </Col>
                        <Col span="18">
                          <div style="padding: 0px 5px; font-size: 16px; color: #7B7B7B;">
                            应用使用情况
                          </div>
                          <div style="padding: 5px; font-size: 28px; font-weight: 500; color: #000;">
                            {{appCount}}
                          </div>
                          <div style="padding: 0px 5px; font-size: 14px; color: #7B7B7B;">
                            应用总数
                          </div>
                        </Col>
                      </Row>
                    </div>
                  </router-link>
                </Col>
                <Col span="6">
                  <router-link :to="{name: 'Configs', params: {}}">
                    <div style="margin:5px; border-left: 4px solid rgb(255, 153, 0); height: 100px; cursor: pointer" >
                      <Row>
                        <Col span="6">
                          <div style="line-height: 100px; text-align: center;">
                            <Icon type="ios-pie-outline" size="30" color="#ff9900"/>
                          </div>
                        </Col>
                        <Col span="18">
                          <div style="padding: 0px 5px; font-size: 16px; color: #7B7B7B;">
                            配置使用情况
                          </div>
                          <div style="padding: 5px; font-size: 28px; font-weight: 500; color: #000;">
                            {{configCount}}
                          </div>
                          <div style="padding: 0px 5px; font-size: 14px; color: #7B7B7B;">
                            配置总数
                          </div>
                        </Col>
                      </Row>
                    </div>
                  </router-link>
                </Col>
                <Col span="6">
                  <div style="margin:5px; border-left: 4px solid rgb(237, 64, 20); height: 100px; cursor: pointer">
                    <Row>
                      <Col span="6">
                        <div style="line-height: 100px; text-align: center;">
                          <Icon type="md-notifications-outline" size="30" color="#ed4014"/>
                        </div>
                      </Col>
                      <Col span="18">
                        <div style="padding: 0px 5px; font-size: 16px; color: #7B7B7B;">
                          Warning 事件
                        </div>
                        <div style="padding: 5px; font-size: 28px; font-weight: 500; color: #000;">
                          {{errorCount}}
                        </div>
                        <div style="padding: 0px 5px; font-size: 14px; color: #7B7B7B;">
                          近7天总报警次数
                        </div>
                      </Col>
                    </Row>
                  </div>
                </Col>
              </Row>
          </TabPane>
        </Tabs>
        <Tabs value="favorite-app" :style="{marginTop: '10px'}">
          <TabPane label="关注应用" name="favorite-app" >
            <Row>
              <Col span="17" style="padding: 0px 5px;">
                <div v-for="(app, index) in favoriteAppList" @click="toView(app.appCode)">
                  <Card style="width:320px;width: 100%;cursor: pointer;margin-bottom: 10px;">
                    <div style="text-align:center;">
                      <Row>
                        <Col span="16" style="text-align: left">
                          <div class="favorite-title">
                            <Icon type="ios-arrow-forward" class="favorite-title-icon"/>
                            {{app.appCode}}（{{app.appName}}）
                          </div>
                          <div class="favorite-descript">简介：{{app.appDesc === '' ? '暂无简介' : app.appDesc}}</div>
                          <div class="favorite-descript">负责人：
                            <span> {{app.appOwner}}</span>
                            <span v-for="member of app.appMember"> {{'/ ' + member}}</span>
                          </div>
                        </Col>
                        <Col span="1" style="text-align: left">
                          <Divider type="vertical" :style="{height: '82px'}"/>
                        </Col>
                        <Col span="7" style="text-align: left">
                          <div class="favorite-change">
                            最近变更
                            <Icon @click="unFavorite('APP', app.appCode, index)" type="ios-heart" size="24" style="float: right; color: #ed8093; position: relative; top: -3px"></Icon>
                          </div>
                          <div class="favorite-change-message">
                            {{app.lastOperator}} 变更于 {{app.gmtModified}}
                          </div>
                          <div class="favorite-change-icon">
                            <Tag type="border" color="#19be6b" :style="{borderRadius: '10px'}">
                              <Icon size="15px" type="md-checkmark-circle-outline"></Icon>
                              已完成
                            </Tag>
                          </div>
                        </Col>
                      </Row>
                    </div>
                  </Card>
                </div>
                <Card style="cursor: pointer;">
                  没有更多关注的应用
                </Card>
              </Col>
              <Col span="7" class="message-notice" style="max-width: 400px;">
                <Card style="width: 100%; margin-bottom: 10px;">
                  <p slot="title">
                    <Icon type="ios-chatbubbles-outline" size="16" class="icon-top-1" tyle="margin-right: 5px;"/>
                    近期变更
                  </p>
                  <div v-for="event of eventList" :class="event.eventLevel === 'info' ? 'notice-info' : (event.eventLevel === 'success' ? 'notice-success' : 'notice-error')">
                   <span class="notice-date">{{event.gmtCreate}} </span>
                   <span class="notice-message">{{event.eventMessage}} {{ event.eventLevel === 'info' ? '' : (event.eventLevel === 'success' ? '(成功)' : '(异常)') }}</span>
                  </div>
                </Card>
              </Col>
            </Row>
          </TabPane>
        </Tabs>
      </Col>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "Home",
    data () {
      return {
        nodeCount: 0,
        appCount: 0,
        configCount: 0,
        errorCount: 0,
        eventList: [{

        }],
        favoriteAppList: [],
      }
    },
    mounted (){
      this.loadHomepage()
    },
    methods: {
      toView: function(appCode){
        this.$router.push({ name: 'Configs', params: { appCode: appCode }})
      },
      unFavorite: function(type, object, index){
        let config = {}
        config.params = {
          favoriteType: type,
          favoriteObject: object,
        }
        let _this = this
        _this.tableLoading = true
        _this.$network.get('/jswitch/user/favorite/remove', config, function (data) {
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '取消关注成功'
          })
          _this.favoriteAppList.splice(index, 1)
        })
      },
      loadHomepage: function () {
        let _this = this
        _this.$network.post('/jswitch/home', {}, function (data) {
          _this.nodeCount = data.nodeCount
          _this.appCount = data.appCount
          _this.configCount = data.configCount
          _this.errorCount = data.errorCount
          _this.eventList = []
          for (let event of data.eventList){
            _this.eventList.push({
              eventMessage: event.eventMessage,
              gmtCreate: event.gmtCreate,
              eventLevel: event.eventLevel,
            })
          }
          if (data.favoriteAppList != null){
            for (let event of data.favoriteAppList){
              _this.favoriteAppList.push({
                appCode: event.appCode,
                appName: event.appName,
                appDesc: event.appDesc,
                appOwner: event.appOwner,
                appMember: event.appMember,
                lastOperator: event.lastOperator,
                gmtModified: event.gmtModified,
              })
            }
          }
        })
      }
    }
  }
</script>

<style>
  .favorite-title {
    font-size:16px;
    font-weight: bold;
    overflow: hidden;
    word-break:keep-all;
    white-space:nowrap;
    text-overflow:ellipsis;
    color: #242424;
  }
  .favorite-title-icon {
    size: 16px;
    position: relative;
    top: -1.5px;
    color: rgb(25, 190, 107);
  }
  .favorite-descript{
    margin-top: 7px;
    color: #7B7B7B;
    font-size: 14px;
    overflow: hidden;
    word-break:keep-all;
    white-space:nowrap;
    text-overflow:ellipsis;
  }
  .favorite-change{
    font-size: 14px;
    overflow: hidden;
    word-break:keep-all;
    white-space:nowrap;
    text-overflow:ellipsis;
    color: #242424;
  }
  .favorite-change-message{
    margin-top: 7px;
    font-size: 14px;
    overflow: hidden;
    word-break:keep-all;
    white-space:nowrap;
    text-overflow:ellipsis;
  }
  .favorite-change-icon{
    margin-top: 6px;
    font-size: 14px;
    overflow: hidden;
    word-break:keep-all;
    white-space:nowrap;
    text-overflow:ellipsis;
  }
  .message-notice {
    padding-left: 13px;
  }
  .message-notice .ivu-card-head {
     padding: 8px 16px;
  }
  .notice-error{
    font-size: 14px;
    color: #ed4014;
    display: flex;
    padding: 3px;
  }
  .notice-success{
    font-size: 14px;
    color: #14a457;
    display: flex;
    padding: 3px;
  }
  .notice-info{
    font-size: 14px;
    color: #666;
    display: flex;
    padding: 3px;
  }
  .notice-date{
    display: inline-block;
    color: #2b2b2b;
    width: 78px;
  }
  .notice-message{
    flex: 1;
    /* word-break:break-all; */
  }
  .help .ivu-tag-text{
    color: #242424 !important;
    clear: both;
    font-size: 14px;
    height: 20px;
    line-height: 20px;
  }
  .help .ivu-tag{
    border-radius: 0;
    width: 100%;
    padding: 10px;
    height: auto;
    border: 1px solid #8799cb;
    background-color: #f6f6f7 !important;
  }
  .ivu-tag-border{
    line-height: 22px;
  }
  .ivu-tabs-nav .ivu-tabs-tab .ivu-icon {
    margin-right: 0;
  }
</style>
