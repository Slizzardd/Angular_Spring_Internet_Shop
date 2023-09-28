import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class UserSharedService {
  private sharedEntityKey = 'actual_user'; // Key for localStorage

  setSharedEntity(entity: any) {
    localStorage.setItem(this.sharedEntityKey, JSON.stringify(entity));
  }

  getSharedEntity() {
    const storedEntity = localStorage.getItem(this.sharedEntityKey);
    return storedEntity ? JSON.parse(storedEntity) : null;
  }

  clearSharedEntity() {
    localStorage.removeItem(this.sharedEntityKey);
  }
}
