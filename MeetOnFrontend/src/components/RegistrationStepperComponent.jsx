import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import MuiThemeProvider from '@material-ui/core/styles/MuiThemeProvider';
import {DoneOutline, WarningIcon} from '@material-ui/icons';

const styles = theme => ({
    
  });

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
  },
  icon: {
    color: "#CD5642 !important"
  },
}));

function getSteps() {
  return ['Log in using Google', 'Create an username', 'Enjoy meeton!'];
}

export default function RegistrationStepperComponent() {
  const classes = useStyles();
  const activeStep = 1;
  const steps = getSteps();

  return (
    <div className={classes.root}>
      <Stepper activeStep={activeStep} alternativeLabel>
        {steps.map((label) => (
          <Step key={label}>
            <StepLabel StepIconProps={{
        classes: {
            completed: classes.icon,
            active: classes.icon
        }}}>{label}</StepLabel>
          </Step>
        ))}
      </Stepper>
    </div>
  );
}