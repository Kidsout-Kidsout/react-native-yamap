export function createAbortError(reason?: string) {
  const ab = new AbortController();
  ab.abort(reason);
  try {
    ab.signal.throwIfAborted();
    const err = new DOMException(reason, 'AbortError');
    throw err;
  } catch (error) {
    return error;
  }
}
