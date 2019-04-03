import { User } from './user';

export class UserList {
    private _allCount : number;
    private _users : User[] = [];


    constructor(data: any) {
        this._allCount = data._allCount;
        for (let i = 0; i < data.users.length; i++) {
            this._users.push(new User(data.users[i]));
        }
    }


    get allCount(): number {
        return this._allCount;
    }

    set allCount(value: number) {
        this._allCount = value;
    }

    get users(): User[] {
        return this._users;
    }

    set users(value: User[]) {
        this._users = value;
    }
}
