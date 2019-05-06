import { User } from './user';

export class UserList {
    private _allCount: number;
    private _users: User[] = [];

    constructor(data: any) {
        this._allCount = data.allCount;
        data.users.forEach(user => {
            this._users.push(new User(user));
        });
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
