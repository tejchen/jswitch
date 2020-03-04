<template>
  <div>
    <Breadcrumb>
      <BreadcrumbItem to="/home">
        <Icon type="ios-home"></Icon> 动态配置中心
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
          <TabPane label="数据看板" name="favorite-data" icon="ios-pie-outline">
          </TabPane>
          <TabPane label="关注应用" name="favorite-app" icon="ios-heart">
            <Row>
              <Col span="12" style="padding: 3px">
                <Card style="width:320px;width: 100%;cursor: pointer">
                  <div style="text-align:center;">
                    <Row>
                      <Col span="14" style="text-align: left">
                        <div style="font-weight: bold; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">TEST_APP（测试应用）</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">简介：这是一个测试应用。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">负责人：admin / chentaijie / tejchen / lalala / chentaijie / chentaijie / chentaijie</div>
                      </Col>
                      <Col span="1" style="text-align: left">
                          <Divider type="vertical" :style="{height: '67px'}"/>
                      </Col>
                      <Col span="9" style="text-align: left">
                        <div style="font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          最近发布
                          <Icon type="ios-heart" size="18" :style="{float: 'right', color: '#ed8093'}"></Icon>
                        </div>
                        <div style="margin-top: 5px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">admin 发布于 6 小时前</div>
                        <div style="margin-top: 4px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          <Tag type="border" color="#3d9e75" :style="{borderRadius: '10px'}">
                            <Icon size="15px" type="md-checkmark-circle-outline"></Icon>
                            已完成
                          </Tag>
                        </div>
                      </Col>
                    </Row>
                  </div>
                </Card>
              </Col>
              <Col span="12" style="padding: 3px">
                <Card style="width:320px;width: 100%;cursor: pointer">
                  <div style="text-align:center;">
                    <Row>
                      <Col span="14" style="text-align: left">
                        <div style="font-weight: bold; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">TEST_APP（测试应用）</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">简介：这是一个测试应用。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">负责人：admin / chentaijie / tejchen / lalala / chentaijie / chentaijie / chentaijie</div>
                      </Col>
                      <Col span="1" style="text-align: left">
                        <Divider type="vertical" :style="{height: '67px'}"/>
                      </Col>
                      <Col span="9" style="text-align: left">
                        <div style="font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          最近发布
                          <Icon type="ios-heart" size="18" :style="{float: 'right', color: '#ed8093'}"></Icon>
                        </div>
                        <div style="margin-top: 5px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">admin 发布于 6 小时前</div>
                        <div style="margin-top: 4px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          <Tag type="border" color="#3d9e75" :style="{borderRadius: '10px'}">
                            <Icon size="15px" type="md-checkmark-circle-outline"></Icon>
                            已完成
                          </Tag>
                        </div>
                      </Col>
                    </Row>
                  </div>
                </Card>
              </Col>
              <Col span="12" style="padding: 3px">
                <Card style="width:320px;width: 100%;cursor: pointer">
                  <div style="text-align:center;">
                    <Row>
                      <Col span="14" style="text-align: left">
                        <div style="font-weight: bold; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">TEST_APP（测试应用）</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">简介：这是一个测试应用。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">负责人：admin / chentaijie / tejchen / lalala / chentaijie / chentaijie / chentaijie</div>
                      </Col>
                      <Col span="1" style="text-align: left">
                        <Divider type="vertical" :style="{height: '67px'}"/>
                      </Col>
                      <Col span="9" style="text-align: left">
                        <div style="font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          最近发布
                          <Icon type="ios-heart" size="18" :style="{float: 'right', color: '#ed8093'}"></Icon>
                        </div>
                        <div style="margin-top: 5px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">admin 发布于 6 小时前</div>
                        <div style="margin-top: 4px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          <Tag type="border" color="#3d9e75" :style="{borderRadius: '10px'}">
                            <Icon size="15px" type="md-checkmark-circle-outline"></Icon>
                            已完成
                          </Tag>
                        </div>
                      </Col>
                    </Row>
                  </div>
                </Card>
              </Col>
              <Col span="12" style="padding: 3px">
                <Card style="width:320px;width: 100%;cursor: pointer">
                  <div style="text-align:center;">
                    <Row>
                      <Col span="14" style="text-align: left">
                        <div style="font-weight: bold; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">TEST_APP（测试应用）</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">简介：这是一个测试应用。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">负责人：admin / chentaijie / tejchen / lalala / chentaijie / chentaijie / chentaijie</div>
                      </Col>
                      <Col span="1" style="text-align: left">
                        <Divider type="vertical" :style="{height: '67px'}"/>
                      </Col>
                      <Col span="9" style="text-align: left">
                        <div style="font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          最近发布
                          <Icon type="ios-heart" size="18" :style="{float: 'right', color: '#ed8093'}"></Icon>
                        </div>
                        <div style="margin-top: 5px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">admin 发布于 6 小时前</div>
                        <div style="margin-top: 4px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          <Tag type="border" color="#3d9e75" :style="{borderRadius: '10px'}">
                            <Icon size="15px" type="md-checkmark-circle-outline"></Icon>
                            已完成
                          </Tag>
                        </div>
                      </Col>
                    </Row>
                  </div>
                </Card>
              </Col>
              <Col span="12" style="padding: 3px">
                <Card style="width:320px;width: 100%;cursor: pointer">
                  <div style="text-align:center;">
                    <Row>
                      <Col span="14" style="text-align: left">
                        <div style="font-weight: bold; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">TEST_APP（测试应用）</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">简介：这是一个测试应用。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。</div>
                        <div style="margin-top: 5px; color: #7B7B7B; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">负责人：admin / chentaijie / tejchen / lalala / chentaijie / chentaijie / chentaijie</div>
                      </Col>
                      <Col span="1" style="text-align: left">
                        <Divider type="vertical" :style="{height: '67px'}"/>
                      </Col>
                      <Col span="9" style="text-align: left">
                        <div style="font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          最近发布
                          <Icon type="ios-heart" size="18" :style="{float: 'right', color: '#ed8093'}"></Icon>
                        </div>
                        <div style="margin-top: 5px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">admin 发布于 6 小时前</div>
                        <div style="margin-top: 4px; font-size: 12px; overflow: hidden; word-break:keep-all; white-space:nowrap; text-overflow:ellipsis;">
                          <Tag type="border" color="#3d9e75" :style="{borderRadius: '10px'}">
                            <Icon size="15px" type="md-checkmark-circle-outline"></Icon>
                            已完成
                          </Tag>
                        </div>
                      </Col>
                    </Row>
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
    name: "Home"
  }
</script>

<style>
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
