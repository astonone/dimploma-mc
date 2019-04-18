import { LocalDate } from './local-date';

export class UserDetails {
    private _id : number;
    private _firstName : string;
    private _lastName : string;
    private _nick : string;
    private _about : string;
    private _photoLink : string;
    private _birthday : LocalDate;

    constructor(data: any) {
        this._id = data.id ? data.id : null;
        this._firstName = data.firstName ? data.firstName : null;
        this._lastName = data.lastName ? data.lastName : null;
        this._nick = data.nick ? data.nick : null;
        this._about = data.about ? data.about : null;
        this._photoLink = data.photoLink ? data.photoLink : null;
        this._birthday = data.birthday ? new LocalDate(data.birthday) : null;
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get firstName(): string {
        return this._firstName;
    }

    set firstName(value: string) {
        this._firstName = value;
    }

    get lastName(): string {
        return this._lastName;
    }

    set lastName(value: string) {
        this._lastName = value;
    }

    get nick(): string {
        return this._nick;
    }

    set nick(value: string) {
        this._nick = value;
    }

    get about(): string {
        return this._about;
    }

    set about(value: string) {
        this._about = value;
    }

    get photoLink(): string {
        return this._photoLink;
    }

    set photoLink(value: string) {
        this._photoLink = value;
    }

    get birthday(): LocalDate {
        return this._birthday;
    }

    set birthday(value: LocalDate) {
        this._birthday = value;
    }

    toObject() {
        return {
            id: this.id,
            firstName: this.firstName,
            lastName: this.lastName,
            about: this.about,
            birthday: {
                year: this.birthday.year,
                month: this.birthday.month,
                day: this.birthday.day
            },
            nick: this.nick,
            photoLink: this.photoLink
        }
    }
}
