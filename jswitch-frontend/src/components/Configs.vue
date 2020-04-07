<template>
  <div style="height: 100%">
    <Breadcrumb>
      <BreadcrumbItem to="/configs">
        <Icon type="md-list" class="icon-top-1"/> 配置管理
      </BreadcrumbItem>
    </Breadcrumb>
    <Layout :style="{height:'100%'}">
      <Sider hide-trigger :style="{background: '#fff', height:'100%'}">
        <Input v-model="appKeyword" search placeholder="请输入应用搜索关键字" style="margin-top: 15px;" @on-enter="searchApps"/>
        <Table max-height="600" highlight-row :show-header="false" :loading="searchTableLoading" :columns="searchColumns" :data="searchData" size="small"
               ref="appSelect"
               :style="{marginTop:'5px', overflow: 'hidden'}"
               @on-current-change="appChange">
        </Table>
      </Sider>
      <div style="height: 100%; background: #fff">
        <Divider type="vertical" :style="{height: '100%'}"/>
      </div>
      <Content :style="{background: '#fff', paddingLeft: '10px'}">
        <Row>
          <Col span="8">
            <Input v-model="configKeyword" search placeholder="请输入配置搜索关键字" style="margin-top: 15px;" @on-enter="searchConfigs"/>
          </Col>
          <Col span="12">
            &nbsp;
          </Col>
          <Col span="4">
            <Button type="dashed" class="create-button" @click="createView = true"> + 创建配置</Button>
          </Col>
        </Row>
        <div>
          <Table stripe border :columns="columns" :data="data" size="small" :loading="tableLoading" style="margin-top: 15px" >
            <template slot-scope="{ row }" slot="name">
              <strong>{{ row.name }}</strong>
            </template>
            <template slot-scope="{ row, index }" slot="action">
              <div style="margin: 0 auto">
                <Button type="info" ghost size="small" style="margin-right: 7px; padding: 3px" @click="mode = 'detail';editView = true; tableAction('detail', index)">详情</Button>
                <Button ghost type="error" size="small" style="padding: 3px" @click="mode = 'push';editView = true; tableAction('push', index)">推送</Button>
              </div>
            </template>
          </Table>
        </div>
        <div style="margin-top: 10px">
          <template>
            <Page :total="pageTotal" :page-size="pageSize" :current="currentPage" style="float: right" show-total
                  @on-change="pageChange" />
          </template>
        </div>
      </Content>
    </Layout>


    <!-- 创建窗口-->
    <Drawer
      title="创建配置"
      v-model="createView"
      width="720"
      :mask-closable="true"
      :styles="styles"
    >
      <Form :model="createFormData">
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="配置编码" label-position="top">
              <i-input v-model="createFormData.appConfigCode" placeholder="配置编码">
                <slot slot="prefix"/>
              </i-input>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="配置名称" label-position="top">
              <Input v-model="createFormData.appConfigName" placeholder="配置名称"/>
            </FormItem>
          </Col>
        </Row>
        <FormItem label="配置内容" label-position="top">
          <Input type="textarea" v-model="createFormData.appConfigContent" :rows="6" placeholder="配置内容" />
        </FormItem>
        <FormItem label="配置描述" label-position="top">
          <Input type="textarea" v-model="createFormData.appConfigDesc" :rows="4" placeholder="配置描述" />
        </FormItem>
      </Form>
      <div class="demo-drawer-footer">
        <Button style="margin-right: 8px" @click="createView = false">取消</Button>
        <Button type="primary" @click="createConfig">创建</Button>
      </div>
    </Drawer>


    <!-- 详情/推送窗口-->
    <Drawer
      title="配置详情"
      v-model="editView"
      width="720"
      :mask-closable="true"
      :styles="styles"
      @on-close="closeEditView"
    >
      <Form :model="editFormData">
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="应用编码" label-position="top">
              <i-input v-model="editFormData.appCode" placeholder="应用编码" :disabled="mode == 'push' || mode == 'detail'">
                <slot slot="prefix"/>
              </i-input>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="应用名称" label-position="top">
              <i-input v-model="editFormData.appName" placeholder="应用名称" :disabled="mode == 'push' || mode == 'detail'">
                <slot slot="prefix"/>
              </i-input>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="配置编码" label-position="top">
              <Input v-model="editFormData.appConfigCode" placeholder="配置名称" :disabled="mode == 'push' || mode == 'detail'"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="配置名称" label-position="top">
              <Input v-model="editFormData.appConfigName" placeholder="配置名称" :disabled="mode == 'push'"/>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="配置来源" label-position="top">
              <Input v-model="editFormData.appConfigSource" placeholder="配置来源" :disabled="mode == 'push' || mode == 'detail'"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="配置类型" label-position="top">
              <Input v-model="editFormData.appConfigType" placeholder="配置类型" :disabled="mode == 'push' || mode == 'detail'"/>
            </FormItem>
          </Col>
        </Row>
        <FormItem label="在线机器" label-position="top">
          <br/>
          <Tag v-for="item in editFormData.appNodes" :key="item" :name="item" type="dot" color="success">{{ item }}</Tag>
        </FormItem>
        <FormItem label="配置内容" label-position="top">
          <Input type="textarea" v-model="editFormData.appConfigContent" :rows="6" placeholder="配置内容" :disabled="mode == 'detail'"/>
        </FormItem>
        <FormItem label="配置描述" label-position="top">
          <Input type="textarea" v-model="editFormData.appConfigDesc" :rows="4" placeholder="配置描述" :disabled="mode == 'push'"/>
        </FormItem>
      </Form>
      <div class="demo-drawer-footer">
        <Button style="margin-right: 8px" @click="editView = false">取消</Button>
        <Button type="primary" @click="updateConfig" :style="{display: detailViewButton}">修改</Button>
        <Button type="primary" @click="pushConfig" :style="{display: pushViewButton}">推送</Button>
      </div>
    </Drawer>

    <Modal
      title="推送任务"
      v-model="pushingWindow"
      cancel-text="取消（后台执行）"
      ok-text="确定"
      :mask-closable="false"
      class-name="vertical-center-modal">
		  <div v-for="node in nodes">
        <Spin size="small" :style="{display: node.status == 'pending' ? 'inline-block' : 'none'}">
          <Icon style="color: #ff9900" type="ios-loading" size=15 class="loading-spin-icon-load"></Icon>
          <div style="display: inline-block; color: #ff9900">正在推送</div>
        </Spin>
        <Spin size="small" :style="{display: node.status == 'success' ? 'inline-block' : 'none'}">
          <Icon style="color: #19be6b" type="md-checkmark-circle-outline" size=15 class="success-spin-icon-load"></Icon>
          <div style="display: inline-block; color: #19be6b">推送成功</div>
        </Spin>
        <Spin size="small" :style="{display: node.status == 'fail' ? 'inline-block' : 'none'}">
          <Icon style="color: #ed4014" type="md-close-circle" size=15 class="fail-spin-icon-load"></Icon>
          <div style="display: inline-block; color: #ed4014">推送失败</div>
        </Spin>
        <div style="display: inline-block;vertical-align: middle;font-weight: 400" >
          机器 IP: {{ node.ip }}
        </div>
		  </div>
    </Modal>
  </div>
</template>

<style>
  .loading-spin-icon-load{
    animation: ani-loading-spin 1s linear infinite;
  }
  @keyframes ani-loading-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
  }
  .success-spin-icon-load{
    /*animation: ani-loading-spin 1s linear infinite;*/
  }
  .fail-spin-icon-load{
    /*animation: ani-loading-spin 1s linear infinite;*/
  }
  .demo-spin-col{
    height: 100px;
    position: relative;
    border: 1px solid #eee;
  }
  .create-button{
    color: #2d8cf0;
    margin-top: 15px;
    margin-left: 10px;
    font-weight: bold;
    float: right;
  }
  .ivu-layout-sider .ivu-table-wrapper {
    border: 0
  }
  .ivu-layout-sider .ivu-table:before{
    background: #fff;
  }
  .ivu-layout-sider .ivu-table:after{
    background: #fff;
  }
  .ivu-table-row{
    cursor: pointer;
  }
  .ivu-layout-sider .ivu-table-wrapper>.ivu-spin-fix{
    border: 0;
  }
  /*.ivu-layout-sider-children{*/
  /*  display: flex;*/
  /*  flex-flow: column;*/
  /*}*/
  /*.ivu-layout-sider-children .ivu-table-wrapper{*/
  /*  flex: 1;*/
  /*}*/
  .ivu-input[disabled] {
    cursor: default;
    color: #666;
  }
</style>

<script>
  export default {
    name: "Apps",
    data () {
      return {
        pushingWindow: false,
        split1: 0.15,
        configKeyword: "",
        tableLoading: false,
        pageTotal: 0,
        pageSize: 15,
        currentPage: 1,
        columns: [
          {
            title: '应用编码',
            key: 'appCode',
            width: 150,
          },{
            title: '配置编码',
            key: 'appConfigCode',
          },{
            title: '配置名称',
            key: 'appConfigName',
            width: 180,
          },{
            title: '创建来源',
            key: 'appConfigSource',
            width: 100
          },{
            title: '最后修改时间',
            key: 'lastUpdateTime',
            width: 150
          },{
            title: '操作',
            slot: 'action',
            width: 140
          }
        ],
        data: [],
        // 搜索窗口
        appKeyword: "",
        selectedAppCode: "",
        searchTableLoading: false,
        searchCurrentPage: 1,
        searchPageTotal: 0,
        searchPageSize: 20,
        searchColumns: [
          {
            title: 'appKeyword',
            key: 'appCode',
            render: (h, params) => {
              let favoriteIcon =h('Icon', {
                props: {
                  type: 'ios-heart',
                  size: 18,
                },
                class: 'heart_icon_favorite',
                style: {
                  display: params.row.isFavorite === 'N' ? 'none' : 'inline-block',
                },
                on:{
                  click: (event) => {
                    this.unFavorite(event, 'APP', params.row.appCode)
                  },
                }
              });
              let unFavoriteIcon =h('Icon', {
                props: {
                  type: 'ios-heart-outline',
                  size: 18,
                },
                class: 'heart_icon',
                style: {
                  display: params.row.isFavorite === 'Y' ? 'none' : 'inline-block',
                },
                on: {
                  click: (event) => {
                    this.favorite(event, 'APP', params.row.appCode)
                  },
                }
              });
              let content = h('span', params.row.appCode)
              return h('div', [
                favoriteIcon,
                unFavoriteIcon,
                content
              ]);
            }
          }
        ],
        searchData: [],
        // 创建窗口
        createView: false,
        editView: false,
        styles: {
          height: 'calc(100% - 55px)',
          overflow: 'auto',
          paddingBottom: '53px',
          position: 'static'
        },
        emptyCreateFormData: {
          appConfigCode: '',
          appConfigName: '',
          appConfigDesc: '',
          appConfigContent: '',
        },
        createFormData: {
          appConfigCode: '',
          appConfigName: '',
          appConfigDesc: '',
          appConfigContent: '',
        },
        mode: 'detail',
        edited: false,
        emptyEditFormData: {
          appCode: '',
          appName: '',
          appConfigCode: '',
          appConfigName: '',
          appConfigType: '',
          appConfigDesc: '',
          appConfigSource: '',
          appNodes: [],
          appConfigContent: '',
          appConfigContentSource: '',
        },
        editFormData: {
          appCode: '',
          appName: '',
          appConfigCode: '',
          appConfigName: '',
          appConfigType: '',
          appConfigDesc: '',
          appConfigSource: '',
          appNodes: [],
          appConfigContent: '',
          appConfigContentSource: '',
        },
        pushViewButton : 'none',
        detailViewButton: 'none',
        nodes: [{
          'ip': '127.0.0.1',
          'status': 1,
          'token': '45678'
        },{
          'ip': '127.0.0.2',
          'status': 2,
          'token': '45678'
        }],
      }
    },
    mounted (){
      if (this.$route.params.appCode != null){
        this.appKeyword = this.$route.params.appCode
      }
      this.loadSearchTable(this.appKeyword, this.searchCurrentPage, this.searchPageSize)
    },
    methods: {
      favorite: function(event, type, object){
        let config = {}
        config.params = {
          favoriteType: type,
          favoriteObject: object,
        }
        let _this = this
        _this.tableLoading = true
        _this.$network.get('/jswitch/user/favorite/add', config, function (data) {
          for(let item of _this.data){
            if (item.appCode === object){
              item.isFavorite = 'Y'
            }
          }
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '关注成功'
          });
        })
        _this.tableLoading = false
        // 切换图标
        event.target.style.display = 'None'
        event.target.previousSibling.style.display = 'inline-block'
      },
      unFavorite: function(event, type, object){
        let config = {}
        config.params = {
          favoriteType: type,
          favoriteObject: object,
        }
        let _this = this
        _this.tableLoading = true
        _this.$network.get('/jswitch/user/favorite/remove', config, function (data) {
          for(let item of _this.data){
            if (item.appCode === object){
              item.isFavorite = 'N'
            }
          }
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '取消关注成功'
          });
        })
        _this.tableLoading = false
        event.target.style.display = 'None'
        event.target.nextSibling.style.display = 'inline-block'
      },
      createConfig: function(){
        if (this.createFormData.appConfigCode === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '配置编码不能为空'
          });
          return
        }
        if (this.createFormData.appConfigName === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '配置名称不能为空'
          });
          return
        }
        if (this.createFormData.appConfigContent === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '配置内容不能为空'
          });
          return
        }
        if (this.selectedAppCode === ""){
          this.$Notice.error({
            title: '操作提醒',
            desc:  '请选择应用'
          })
        }
        let config = JSON.parse(JSON.stringify(this.createFormData))
        config.appCode = this.selectedAppCode
        let _this = this
        _this.$network.post('/jswitch/app/config/save', config, function (data) {
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '创建成功'
          })
          _this.createView = false
          _this.createFormData =  JSON.parse(JSON.stringify(_this.emptyCreateFormData))
          _this.loadTable(_this.configKeyword, _this.currentPage, _this.pageSize)
        })
      },
      pushConfig: function() {
        // 比较数据变动
        if (this.editFormData.appConfigContentSource === this.editFormData.appConfigContent) {
          this.$Notice.warning({
            title: '操作提醒',
            desc:  '配置未变更'
          })
          return
        }
        let config = JSON.parse(JSON.stringify(this.editFormData))
        let _this = this
        _this.$network.post('/jswitch/app/config/push', config, function (data) {
          if (data.appNodes.length === 0) {
            _this.$Notice.success({
              title: '操作提醒',
              desc:  '修改成功，目前该应用没有在线机器'
            })
          }else{
            _this.$Notice.success({
              title: '操作提醒',
              desc:  '提交推送任务成功'
            })
            // 展示推送任务
            var appNodeTokens = []
            let appVersion = data.appVersion
            let appCode = data.appCode
            _this.nodes = []
            for (var node of data.appNodes){
              _this.nodes.push({
                'ip': node.appNodeIp,
                'token': node.appNodeToken,
                'status': 'pending',
              })
              appNodeTokens.push(node.appNodeToken)
            }
            console.log(appNodeTokens)
            // 启动默认轮训
            let counter = 0
            _this.timer = setInterval(function () {
              if (counter >= 20){
                clearInterval(_this.timer)
                // 超时失败
                for(var node of _this.nodes){
                  if (node.status == 'pending'){
                    node.status = "fail"
                  }
                }
                return
              }
              let params = {
                appCode: appCode,
                appVersion: appVersion,
                appNodeTokens: appNodeTokens,
              }
              _this.$network.post('/jswitch/app/config/pushResult', params, function (data) {
                for(var token in data){
                  for(var node of _this.nodes){
                    // 更新该节点数据
                    if (token === node.token){
                      node.status = data[token]
                    }
                  }
                }
                // 如果全部节点结束，则取消该定时器
                var flag = true
                for(var node of _this.nodes){
                  if (node.status == 'pending'){
                    flag = false
                  }
                }
                if (flag) {
                  clearInterval(_this.timer)
                }else{
                  counter++
                }
              })
            }, 1000)
            _this.pushingWindow = true
          }
          _this.edited = true
        })
      },
      updateConfig: function() {
        let config = JSON.parse(JSON.stringify(this.editFormData))
        let _this = this
        _this.$network.post('/jswitch/app/config/update', config, function (data) {
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '更新成功'
          })
          _this.edited = true
        })
      },
      closeEditView: function() {
        this.editFormData =  JSON.parse(JSON.stringify(this.emptyEditFormData))
        if(this.edited) {
          this.loadTable(this.keyword, this.currentPage, this.pageSize)
        }
        this.edited = false
        clearInterval(this.timer)
      },
      appChange: function(currentRow){
        this.selectedAppCode = currentRow.appCode
        this.loadTable(this.configKeyword, 0, this.pageSize)
      },
      searchApps: function () {
        this.loadSearchTable(this.appKeyword, this.searchCurrentPage, this.searchPageSize)
      },
      searchConfigs: function () {
        this.loadTable(this.configKeyword, this.currentPage, this.pageSize)
      },
      pageChange: function (page) {
        this.loadTable(this.configKeyword, page, this.pageSize)
      },
      tableAction: function (action, index) {
        if (action == 'push') {
          this.pushViewButton = 'inline-block'
          this.detailViewButton = 'none'
          this.editFormData.appConfigCode = this.data[index].appConfigCode
        }
        if (action == 'detail') {
          this.detailViewButton = 'inline-block'
          this.pushViewButton = 'none'
          this.editFormData.appConfigCode = this.data[index].appConfigCode
        }
        // 加载数据
        let appCode = this.selectedAppCode
        let appConfigCode = this.editFormData.appConfigCode
        let form = {};
        form.params = {
          'appCode': appCode,
        }
        let uri = '/jswitch/app/config/detail/'+appConfigCode
        let _this = this
        _this.$network.get(uri, form, function (data) {
          if (data == null){
            return
          }
          _this.editFormData.appCode = data.appCode
          _this.editFormData.appName = data.appName
          _this.editFormData.appConfigCode = data.appConfigCode
          _this.editFormData.appConfigName = data.appConfigName
          _this.editFormData.appConfigType = data.appConfigType
          _this.editFormData.appConfigDesc = data.appConfigDesc
          _this.editFormData.appConfigSource = data.appConfigSource
          _this.editFormData.appConfigContent = data.appConfigContent
          _this.editFormData.appConfigContentSource = data.appConfigContent
          // 生成ip地址
          _this.editFormData.appNodes = []
          if (data.appNodes.length === 0){
            _this.editFormData.appNodes.push("暂无在线机器")
          }else{
            for(let node of data.appNodes){
              _this.editFormData.appNodes.push("IP: " + node)
            }
          }

        })
      },
      loadSearchTable: function (keyword, page, pageSize) {
        let config = {};
        config.params = {
          keyword: keyword,
          page: page,
          pageSize: pageSize
        }
        let _this = this
        _this.searchTableLoading = true
        _this.$network.get('/jswitch/app/list', config, function (data) {
          if (data == null){
            return
          }
          console.log(data)
          _this.searchData = []
          if (data['dataList'] != null){
            for (let item of data['dataList']){
              _this.searchData.push({
                appCode: item['appCode'],
                appName: item['appName'],
                isFavorite: item['isFavorite'],
              })
            }
            if(_this.searchData.length > 0){
              //查找完全想等的数据
              let selected = false
              for (let item of _this.searchData){
                if (item.appCode === keyword){
                  selected = true
                  item._highlight = true
                  _this.selectedAppCode = item.appCode
                  _this.loadTable(_this.configKeyword, 0, _this.pageSize)
                }
              }
              //默认选中第一个
              if (!selected){
                _this.searchData[0]._highlight = true
                _this.selectedAppCode = _this.searchData[0].appCode
                _this.loadTable(_this.configKeyword, 0, _this.pageSize)
              }
            }
          }
          _this.searchTableLoading = false
        })
      },
      loadTable: function (keyword, page, pageSize) {
        let config = {};
        config.params = {
          appCode: this.selectedAppCode,
          keyword: keyword,
          page: page,
          pageSize: pageSize
        }
        let _this = this
        _this.tableLoading = true
        _this.$network.get('/jswitch/app/config/list', config, function (data) {
          if (data == null){
            return
          }
          _this.pageTotal = data['total']
          _this.currentPage = data['pageNo']
          _this.pageSize = data['pageSize']
          _this.data = []
          if (data['dataList'] != null){
            for (let item of data['dataList']){
              _this.data.push({
                appCode: item['appCode'],
                appName: item['appName'],
                appConfigCode: item['appConfigCode'],
                appConfigName: item['appConfigName'],
                appConfigSource: item['appConfigSource'],
                lastUpdateTime: item['gmtModified'],
              })
            }
          }
          _this.tableLoading = false
        })
      }
    }
  }
</script>
