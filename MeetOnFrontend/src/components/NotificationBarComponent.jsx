import React from 'react';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import AuthService from "../services/AuthService";
import NotificationService from "../services/NotificationService";
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import NotificationsNoneOutlinedIcon from '@material-ui/icons/NotificationsNoneOutlined';
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';
import SockJsClient from 'react-stomp'
import { WS_BASE_URL } from '../constants/constant';

export default class TemporaryDrawer extends React.Component{
  constructor(props) {
      super(props);
      this.state = {
        currentUser: AuthService.getCurrentUser(),
        classes: null,
        notifications: null,
        isNotificationListOpened: false,
        unviewedAmount: 0,
        notificationIsOpened: false,

    }
    
    this.toggleDrawer.bind(this);
  }
  componentDidMount() {
    NotificationService.getNotificationsByUser(this.state.currentUser.id).then((res) => {
      this.setState({ notifications: res.data });
      this.setState({ unviewedAmount: this.state.notifications?.filter(notification => { return notification.status === "UNVIEWED" }).length})
      this.setState({ notificationIsOpened: this.state.unviewedAmount > 0 });
    })
  }

    toggleDrawer = (open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift'))
      return;

    this.setState({ isNotificationListOpened: open });
  }

  handleNotification = (notification) => {
    console.log(this.state.notifications)
    this.state.notifications.unshift(notification)
    this.setState({notifications: this.state.notifications})
    this.setState({unviewedAmount: this.state.notifications?.filter(notification => { return notification.status === "UNVIEWED" }).length})
    this.setState({notificationIsOpened: this.state.unviewedAmount > 0 });
    console.log(this.state.notifications)
  }

  checkViewed=(status) => {
    return status === "UNVIEWED" ? {backgroundColor:"#dadada"} : {backgroundColor: "white"};
  }

  handleClose = (event, reason) => {
    console.log("closing")
    if (reason === 'clickaway')
        return;

    this.setState({ notificationIsOpened: false })
  };

  setViewed=(index) => {
    if(this.state.notifications[index].status === "UNVIEWED") {
      NotificationService.setNotificationStatusViewed(this.state.notifications[index].id).then((res) => {
        this.state.notifications[index]=res.data;
        this.setState({
            notifications: this.state.notifications,
            unviewedAmount: this.state.unviewedAmount - 1     
        });
      })
    }
  }

  showShackbar(notificaton) {
    if (notificaton.status === "UNVIEWED") {
      console.log("SNACKBAR OPENED!!!");
      return (
      <Snackbar open="true" autoHideDuration={6000} onClose={this.handleClose} anchorOrigin={{vertical: 'bottom', horizontal: 'left'}}>
        <MuiAlert elevation={6} variant="filled" onClose={this.handleClose} severity="info">
            {notificaton.content}
        </MuiAlert>
      </Snackbar>
      )}
  }

  list = () => (
    <div role="presentation" onKeyDown={this.toggleDrawer(false)}>
      <List style={{width: "500px"}}> 
      {
        this.state.notifications?.map((notification, index) => (
        <div>
            <ListItem button key={index} style={this.checkViewed(notification.status)} onClick={this.setViewed.bind(this, index)}>
                <ListItemText>
                  <p>{notification.content}</p>
                  <p style={{color: "#aaa", fontSize: "12px", margin: "10px 0 0 0"}}>{notification.date.replace("T"," ")}</p>
                </ListItemText>
            </ListItem>
            <Divider />
        </div>
        ))
      }
      </List>
    </div>
  );

  render() {
    return (
      <div>
      {
        <React.Fragment>
          <IconButton aria-label="show 4 new mails" color="inherit" onClick={this.toggleDrawer(true)} style={{marginTop: "2px"}}>
            <Badge badgeContent={this.state.unviewedAmount} color="secondary">
              <NotificationsNoneOutlinedIcon/>
            </Badge>
          </IconButton>
          <Drawer anchor="right" style={{flexShrink: 0}} open={this.state.isNotificationListOpened} onClose={this.toggleDrawer(false)}>
            {this.list()}
          </Drawer>
        </React.Fragment>
      }
        <div>
          <Snackbar open={this.state.notificationIsOpened && this.state.unviewedAmount > 0} autoHideDuration={6000} onClose={this.handleClose} anchorOrigin={{vertical: 'bottom', horizontal: 'left'}}>
            <MuiAlert style={{marginBottom: "10px"}} elevation={6} variant="filled" onClose={this.handleClose} severity="info">
              You have {this.state.unviewedAmount} new notifications!
            </MuiAlert>
          </Snackbar>
        </div>
          <SockJsClient
              url={WS_BASE_URL + `/ws`}
              topics={[`/user/${this.state.currentUser.id}/queue/notify`]}
              onMessage={(notification) => this.handleNotification(notification)} />
      </div>
    );
  }
}
