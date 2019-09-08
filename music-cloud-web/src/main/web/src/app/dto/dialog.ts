import { LocalDate } from './local-date';
import { User } from './user';
import { Message } from './message';

export class Dialog {
    private _id: number;
    private _name: string;
    private _time: LocalDate;
    private _users: User[] = [];
    private _messages: Message[] = [];

    public static createEmptyDialog() {
        return new Dialog({
            id: -1,
            name: '',
            time: {},
            users: [],
            messages: []
        });
    }


    constructor(data: any) {
        this._id = data.id;
        this._name = data.name;
        this._time = new LocalDate(data.time);
        data.users.forEach(user => {
            this._users.push(new User(user));
        });
        data.messages.forEach(message => {
            this._messages.push(new Message(message));
        });
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get name(): string {
        return this._name;
    }

    set name(value: string) {
        this._name = value;
    }

    get time(): LocalDate {
        return this._time;
    }

    set time(value: LocalDate) {
        this._time = value;
    }

    get users(): User[] {
        return this._users;
    }

    set users(value: User[]) {
        this._users = value;
    }

    get messages(): Message[] {
        return this._messages;
    }

    set messages(value: Message[]) {
        this._messages = value;
    }
}
