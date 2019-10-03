import {Grid} from '@material-ui/core';
import React from 'react';

export const stepName = 'Preview data';

function PreviewStep({ mapping }) {
    console.log(mapping);
    return (
      <Grid container>
        Preview
      </Grid>
    );
}

export default PreviewStep;