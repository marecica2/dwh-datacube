import React, { useContext } from 'react';
import { LinearProgress } from '@material-ui/core';
import { AppContext } from '../../context/AppContext';

export default function ImportJob() {
  const { state } = useContext(AppContext);
  return (
    <div>
      {state.importStatus && state.importStatus.running && (
        <div>
          {Object.entries(state.progresses).map(([key, progress]) => (
            <div key={key}>
              <p>
                <span>{`Reading ${key} `}</span>
                {progress.rowsCount}
                <span> rows of </span>
                {progress.totalRowsCount}
              </p>
              <LinearProgress
                variant="determinate" color="secondary"
                value={(progress.rowsCount / progress.totalRowsCount) * 100}
              />
            </div>
            ))}
        </div>
      )}
    </div>
  )
}