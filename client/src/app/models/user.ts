export interface IUser {
  id: number
  email: string
  created: string
  updated: string
  phoneNumber: string
  firstName: string
  lastName: string
  password: string
  role: string
  authToken?: string | null;
}
