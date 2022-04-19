import React, {useEffect} from 'react';
import Popover from '@material-ui/core/Popover';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import {Rating} from "@material-ui/lab";
import {Box} from "@material-ui/core";



export default function MeetingRating(props) {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [value, setValue] = React.useState(0);

    React.useEffect(() => {
        setValue(props.rating);
    }, [props.rating])

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };


    const handleRatingChange = (newValue) => {
        console.log(props);
        props.onRatingChange(newValue);
    }

    const open = Boolean(anchorEl);
    const id = open ? 'simple-popover' : undefined;

    return (
        <div>
            <Button aria-describedby={id} variant="contained" color="primary" onClick={handleClick}>
                Rate
            </Button>
            <Popover
                id={id}
                open={open}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'center',
                }}
            >
                <Box component="fieldset" mb={3} borderColor="transparent">
                    <Typography component="legend">Your score</Typography>
                    <Rating
                        name="simple-controlled"
                        value={value}
                        onChange={(event, newValue) => {
                            setValue(newValue);
                            handleRatingChange(newValue);
                        }}
                    />
                </Box>
            </Popover>
        </div>
    );
}