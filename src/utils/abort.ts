export function createAbortError(reason?: string) {
  const err = new Error(reason);
  err.name = 'AbortError';
  throw err;
}
