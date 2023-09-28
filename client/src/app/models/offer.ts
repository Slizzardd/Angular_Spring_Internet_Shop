import {IUser} from "./user";

export interface IOffer {
  userId: number,
  user: IUser,
  linkForPayment: number,
  address: string,
  status: string
}
