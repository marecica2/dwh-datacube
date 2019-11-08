export default function msToTime(s) {
  const ms = s % 1000;
  s = (s - ms) / 1000;
  const secs = s % 60;
  s = (s - secs) / 60;
  const mins = s % 60;
  const hrs = (s - mins) / 60;
  return hrs > 0 ? (hrs + ' hrs') : '' +
  mins > 0 ? (mins + ' min') : '' +
  secs > 0 ? (secs + ' sec') : '' +
  ms > 0 ? (ms + ' ms') : '';
}