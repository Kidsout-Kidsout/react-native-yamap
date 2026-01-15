export const invariant = (e: string | Error): never => {
  if (e instanceof Error) throw e;
  throw new Error(e);
};
