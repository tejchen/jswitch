<template>
  <div>
    <Breadcrumb>
      <BreadcrumbItem to="/apps">
        <Icon type="ios-apps" class="icon-top-1"></Icon> 应用管理
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
            <Button :to="{name: 'Configs', params: {appCode: row.appCode}}" type="info" ghost size="small" style="margin-right: 7px; padding: 3px">查看配置</Button>
            <Button ghost type="error" size="small" style="padding: 3px" @click="editView = true; tableAction(index)">修改</Button>
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
      width="600"
      :mask-closable="true"
      :styles="styles"
      @on-close="closeCreateView"
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
          <Col span="12">
            <FormItem label="签名校验" label-position="top" >
              <br>
              <i-switch v-model="createFormData.appCheckSign">

              </i-switch>
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
            <FormItem label="应用版本" label-position="top" >
              <Input v-model="createFormData.appVersion" placeholder="应用创建完成后,自动生成" :disabled="true"/>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="应用负责人" label-position="top">
              <Select v-model="createFormData.appOwner" placeholder="应用负责人">
                <Option value="admin">admin</Option>
                <Option value="tejchen">tejchen</Option>
                <Option value="qx">qx</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="应用秘钥" label-position="top" >
              <Input v-model="createFormData.appSignKey" placeholder="应用创建完成后,自动生成" :disabled="true"/>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="24">
            <FormItem label="应用描述" label-position="top">
              <Input type="textarea" v-model="createFormData.appDesc" :rows="4" placeholder="应用描述" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div class="demo-drawer-footer">
        <Button style="margin-right: 8px" @click="createView = false">取消</Button>
        <Button type="primary" @click="createApp">创建</Button>
      </div>
    </Drawer>

    <!-- 创建窗口 -->
    <Drawer
      title="应用详情"
      v-model="editView"
      width="600"
      :mask-closable="true"
      :styles="styles"
      @on-close="closeEditView"
    >
      <Form :model="editFormData">
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="应用编码" label-position="top">
              <Input v-model="editFormData.appCode" placeholder="应用编码" :disabled="1 == 1">
                <slot slot="prefix"/>
              </Input>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="签名校验" label-position="top" >
              <br>
              <i-switch v-model="editFormData.appCheckSign">

              </i-switch>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="应用名称" label-position="top">
              <Input v-model="editFormData.appName" placeholder="应用名称" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="应用版本" label-position="top" >
              <Input v-model="editFormData.appVersion" placeholder="应用创建完成后,自动生成" :disabled="true"/>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem label="应用负责人" label-position="top">
              <Select v-model="editFormData.appOwner" placeholder="应用负责人">
                <Option value="admin">admin</Option>
                <Option value="tejchen">tejchen</Option>
                <Option value="qx">qx</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="应用秘钥" label-position="top" >
              <Input v-model="editFormData.appSignKey" placeholder="应用创建完成后,自动生成" :disabled="true"/>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="24">
            <FormItem label="应用描述" label-position="top">
              <Input type="textarea" v-model="editFormData.appDesc" :rows="4" placeholder="应用描述" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div class="demo-drawer-footer">
        <Button style="margin-right: 8px" @click="editView = false">取消</Button>
        <Button type="primary" @click="editApp">更新</Button>
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
        keyword: "",
        tableLoading: false,
        pageTotal: 0,
        pageSize: 12,
        currentPage: 1,
        columns: [
          {
            title: '应用编码',
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
          },
          {
            title: '应用名称',
            key: 'appName'
          },
          {
            title: '负责人',
            key: 'appOwner',
            width: 150
          },
          {
            title: '在线机器',
            key: 'onlineMachineCount',
            width: 100
          },
          {
            title: '创建时间',
            key: 'createTime',
            width: 150
          },
          {
            title: '操作',
            slot: 'action',
            width: 150
          }
        ],
        data: [],
        // 创建窗口
        createView: false,
        created: false,
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
          appSignKey: '',
          appVersion: '',
          appCheckSign: false,
          appOwner: 'admin',
        },
        createFormData: {
          appCode: '',
          appName: '',
          appDesc: '',
          appSignKey: '',
          appVersion: '',
          appCheckSign: false,
          appOwner: 'admin',
        },
        // 编辑窗口
        editView: false,
        edited: false,
        emptyEditFormData: {
          appCode: '',
          appName: '',
          appDesc: '',
          appSignKey: '',
          appVersion: '',
          appCheckSign: false,
          appOwner: 'admin',
        },
        editFormData: {
          appCode: '',
          appName: '',
          appDesc: '',
          appSignKey: '',
          appVersion: '',
          appCheckSign: false,
          appOwner: 'admin',
        },
      }
    },
    mounted (){
      this.loadTable(this.keyword, this.currentPage, this.pageSize)
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
        let _this = this
        _this.$network.post('/jswitch/app/save', this.createFormData, function (data) {
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '创建成功'
          })
          _this.createFormData.appSignKey = data.appSignKey
          _this.createFormData.appVersion = 'V ' + data.appVersion
          _this.created = true
        })
      },
      editApp: function(){
        if (this.editFormData.appCode === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '应用编码不能为空'
          });
          return
        }
        if (this.editFormData.appName === ''){
          this.$Notice.warning({
            title: '必填提醒',
            desc:  '应用名称不能为空'
          });
          return
        }
        let _this = this
        _this.$network.post('/jswitch/app/update', this.editFormData, function (data) {
          _this.$Notice.success({
            title: '操作提醒',
            desc:  '更新成功'
          })
          _this.edited = true
        })
      },
      closeCreateView: function () {
        this.createFormData =  JSON.parse(JSON.stringify(this.emptyCreateFormData))
        if (this.created) {
          this.loadTable(this.keyword, this.currentPage, this.pageSize)
        }
        this.created = false
      },
      closeEditView: function () {
        this.editFormData =  JSON.parse(JSON.stringify(this.emptyEditFormData))
        if (this.edited) {
          this.loadTable(this.keyword, this.currentPage, this.pageSize)
        }
        this.edited = false
      },
      tableAction: function(index) {
        let appCode = this.data[index].appCode
        let form = {};
        let uri = '/jswitch/app/detail/'+appCode
        let _this = this
        _this.$network.get(uri, form, function (data) {
          if (data == null){
            return
          }
          console.log(data)
          _this.editFormData.appCode = data.appCode
          _this.editFormData.appName = data.appName
          _this.editFormData.appConfigDesc = data.appConfigDesc == '' ? '暂无描述' : data.appConfigDesc
          _this.editFormData.appNodes = data.appNodes
          _this.editFormData.appCheckSign = data.appCheckSign
          _this.editFormData.appSignKey = data.appSignKey
          _this.editFormData.appDesc = data.appDesc
          _this.editFormData.appVersion = "V " + data.appVersion
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
                'onlineMachineCount': item['onlineMachineCount'] + ' 台',
                'createTime': item['gmtCreate'],
                'isFavorite': item['isFavorite'],
              })
            }
          }
          _this.tableLoading = false
        })
      }
    }
  }
</script>
