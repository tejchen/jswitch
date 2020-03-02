<template>
  <div id ="app" class="layout">
    <Layout>
      <Header>
        <Menu mode="horizontal" theme="dark">
          <div class="layout-logo">
            <img src="@/assets/logo.png" style="height: 100%">
          </div>
          <div class="layout-nav">
            <MenuItem name="1">
              <Icon type="md-settings" />
              设置
            </MenuItem>
            <Submenu name="2">
              <template slot="title">
                <Icon type="ios-contact" size="16"/>
                陈泰杰
              </template>
              <MenuGroup title="行为">
                <MenuItem name="2-1">操作日志</MenuItem>
              </MenuGroup>
              <MenuGroup title="账号">
                <MenuItem name="2-2">退出登录</MenuItem>
              </MenuGroup>
            </Submenu>
          </div>
        </Menu>
      </Header>
      <Layout>
        <Sider hide-trigger :style="{background: '#fff'}">
          <Menu :active-name="$route.path" ref="leftMenu" theme="light" width="auto" :style="{height:'100%'}">
            <MenuItem name="/home" to="/home">
              <Icon type="ios-home" />
              首页
            </MenuItem>
            <MenuItem name="/apps" to="/apps">
              <Icon type="ios-apps" />
              应用管理
            </MenuItem>
            <MenuItem name="/configs" to="/configs">
              <Icon type="md-list" />
              配置管理
            </MenuItem>
<!--            <MenuItem name="1-4" to="/prod">-->
<!--              <Icon type="ios-apps" />-->
<!--              环境管理-->
<!--            </MenuItem>-->
          </Menu>
        </Sider>
        <Layout :style="{padding: '8px'}">
          <Content :style="{padding: '24px', minHeight: '280px', background: '#fff'}">
            <router-view></router-view>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>
<script>
  export default {
    name: "App",
    data () {
      return {
        menuActive: '1-1',
      }
    },
    watch: {
      '$route': function (to, from) {
        console.log(to)
        console.log(from)
      }
    }
  }
</script>
<style>
  html{
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    height: 100%;
  }
  body{
    height: 100%;
  }
  .ivu-layout{
    height: 100%;
  }
  .layout{
    background: #f5f7f9;
    position: relative;
    overflow: hidden;
    height: 100%;
  }
  .layout-logo{
    width: 100px;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 15px;
    left: 20px;
  }
  .layout-nav{
    width: 420px;
    margin: 0 auto;
    margin-right: 30px;
  }
  .ivu-layout-header{
    padding: 0 0px;
  }
  .layout-logo{
    height: 40px;
    background: rgba(0,0,0,0);
    left: 30px;
  }
  .ivu-menu-submenu{
    text-align: left;
  }
  .ivu-menu-item{
    text-align: left;
  }
  .ivu-menu a{
    color: #666;
  }
  .layout-nav{
    float: right;
    width: auto;
  }
</style>
