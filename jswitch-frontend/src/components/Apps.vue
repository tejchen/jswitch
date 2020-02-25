<template>
  <div>
    <Breadcrumb>
      <BreadcrumbItem to="/apps">
        <Icon type="ios-apps"></Icon> 应用管理
      </BreadcrumbItem>
    </Breadcrumb>
    <div>
      <Row>
        <Col span="8">
          <Input v-model="keyword" search placeholder="请输入搜索关键字" style="margin-top: 15px;" @on-enter="searchApps"/>
        </Col>
        <Col span="12">
          &nbsp;
        </Col>
        <Col span="4">
          <Button type="dashed" class="create-button" @click="createView = true"> + 创建应用</Button>
        </Col>
      </Row>
    </div>
    <div>
      <Table stripe border :columns="columns" size="small" :data="data" :loading="tableLoading" style="margin-top: 15px" >
        <template slot-scope="{ row }" slot="name">
          <strong>{{ row.name }}</strong>
        </template>
        <template slot-scope="{ row, index }" slot="action">
          <div style="margin: 0 auto">
            <Button type="info" ghost size="small" style="margin-right: 7px; padding: 3px">详情</Button>
            <Button ghost type="error" size="small" style="padding: 3px">修改</Button>
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



    <!-- 创建窗口 -->
    <Drawer
      title="创建应用"
      v-model="createView"
      width="720"
      :mask-closable="true"
      :styles="styles"
    >
      <Form :model="createFormData">
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="应用编码" label-position="top">
              <Input v-model="createFormData.appCode" placeholder="应用编码" >
                <slot slot="prefix"/>
              </Input>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="应用名称" label-position="top">
              <Input v-model="createFormData.appName" placeholder="应用名称" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="应用负责人" label-position="top">
              <Select v-model="createFormData.appOwner" placeholder="应用负责人">
                <Option value="admin">admin</Option>
                <Option value="tejchen">tejchen</Option>
                <Option value="qx">qx</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <FormItem label="应用描述" label-position="top">
          <Input type="textarea" v-model="createFormData.appDesc" :rows="4" placeholder="应用描述" />
        </FormItem>
      </Form>
      <div class="demo-drawer-footer">
        <Button style="margin-right: 8px" @click="createView = false">取消</Button>
        <Button type="primary" @click="createApp">创建</Button>
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
  .demo-drawer-footer{
    width: 100%;
    position: absolute;
    bottom: 0;
    left: 0;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    background: #fff;
  }
</style>

<script>
  export default {
    name: "Apps",
    data () {
      return {
        keyword: "",
        tableLoading: false,
        pageTotal: 0,
        pageSize: 15,
        currentPage: 1,
        columns: [
          {
            title: '应用编码',
            key: 'appCode'
          },
          {
            title: '应用名称',
            key: 'appName'
          },
          {
            title: '负责人',
            key: 'appOwner'
          },
          {
            title: '在线机器',
            key: 'onlineMachine'
          },
          {
            title: '创建时间',
            key: 'createTime'
          },
          {
            title: '操作',
            slot: 'action'
          }
        ],
        data: [],
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
          appCode: '',
          appName: '',
          appDesc: '',
          appOwner: 'admin',
        },
        createFormData: {
          appCode: '',
          appName: '',
          appDesc: '',
          appOwner: 'admin',
        },
      }
    },
    mounted (){
      this.loadTable(this.keyword, this.currentPage, this.pageSize)
    },
    methods: {
      createApp: function(){
        if (this.createFormData.appCode === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '应用编码不能为空'
          });
          return
        }
        if (this.createFormData.appName === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '应用名称不能为空'
          });
          return
        }
        if (this.createFormData.appOwner === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '应用负责人不能为空'
          });
          return
        }
        let _this = this
        _this.$network.post('/jswitch/app/save', this.createFormData, function (data) {
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '创建成功'
          })
          _this.createView = false
          _this.createFormData =  JSON.parse(JSON.stringify(_this.emptyCreateFormData))
          _this.loadTable(_this.keyword, _this.currentPage, _this.pageSize)
        })
      },
      searchApps: function () {
        this.loadTable(this.keyword, this.currentPage, this.pageSize)
      },
      pageChange: function (page) {
        this.loadTable(this.keyword, page, this.pageSize)
      },
      loadTable: function (keyword, page, pageSize) {
        let config = {};
        config.params = {
          keyword: keyword,
          page: page,
          pageSize: pageSize
        }
        let _this = this
        _this.tableLoading = true
        _this.$network.get('/jswitch/app/list', config, function (data) {
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
                'appCode': item['appCode'],
                'appName': item['appName'],
                'appOwner': item['appOwner'],
                'onlineMachine': item['onlineMachine'],
                'createTime': item['gmtCreate'],
              })
            }
          }
          _this.tableLoading = false
        })
      }
    }
  }
</script>
