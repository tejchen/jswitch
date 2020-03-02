<template>
  <div style="height: 100%">
    <Breadcrumb>
      <BreadcrumbItem to="/configs">
        <Icon type="md-list" /> 配置管理
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
                <Button type="info" ghost size="small" style="margin-right: 7px; padding: 3px">详情</Button>
                <Button ghost type="error" size="small" style="padding: 3px">推送</Button>
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
              <Input v-model="createFormData.appConfigCode" placeholder="配置编码" >
                <slot slot="prefix"/>
              </Input>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="配置名称" label-position="top">
              <Input v-model="createFormData.appConfigName" placeholder="配置名称" />
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
  </div>
</template>

<style>
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
</style>

<script>
  export default {
    name: "Apps",
    data () {
      return {
        split1: 0.15,
        configKeyword: "",
        tableLoading: false,
        pageTotal: 0,
        pageSize: 15,
        currentPage: 1,
        columns: [
          {
            title: '应用编码',
            key: 'appCode'
          },{
            title: '应用名称',
            key: 'appName'
          },{
            title: '配置编码',
            key: 'appConfigCode'
          },{
            title: '配置名称',
            key: 'appConfigName'
          },{
            title: '创建来源',
            key: 'appConfigSource'
          },{
            title: '最后修改时间',
            key: 'lastUpdateTime'
          },{
            title: '操作',
            slot: 'action'
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
            key: 'appCode'
          }
        ],
        searchData: [],
        // 创建窗口
        createView: false,
        createDefaultOwner: 'admin',
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
        }
      }
    },
    mounted (){
      this.loadSearchTable(this.appKeyword, this.searchCurrentPage, this.searchPageSize)
    },
    methods: {
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
      appChange: function(currentRow){
        this.selectedAppCode = currentRow.appCode
        this.loadTable(this.configKeyword, 0, this.pageSize)
      },
      searchApps: function () {
        this.loadSearchTable(this.appKeyword, this.searchCurrentPage, this.searchCurrentPage)
      },
      searchConfigs: function () {
        this.loadTable(this.configKeyword, this.currentPage, this.pageSize)
      },
      pageChange: function (page) {
        this.loadTable(this.configKeyword, page, this.pageSize)
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
          _this.searchPageTotal = data['total']
          _this.searchCurrentPage = data['pageNo']
          _this.searchPageSize = data['pageSize']
          _this.searchData = []
          if (data['dataList'] != null){
            for (let item of data['dataList']){
              _this.searchData.push({
                'appCode': item['appCode'],
                'appName': item['appName'],
              })
            }
            //默认选中第一个
            if(_this.searchData.length > 0){
              _this.searchData[0]._highlight = true
              //触发表格加载
              _this.selectedAppCode = _this.searchData[0].appCode
              _this.loadTable(_this.configKeyword, 0, _this.pageSize)
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
