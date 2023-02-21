export interface IUser {
  id?: number;
  login?: string;
  firstName?: string;
  imageUrl?: string;
}

export class User implements IUser {
  constructor(public id: number, public login: string, public firstName: string, public imageUrl: string) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
