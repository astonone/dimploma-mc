import {LocalDate} from './local-date';
import {UserDetails} from './user-details';

export class User {
    private _id : number;
    private _email : string;
    private _password : string;
    private _newPassword : string;
    private _dateCreate : LocalDate;
    private _userDetails: UserDetails;


    constructor(data : any) {
        this._id = data.id;
        this._email = data.email;
        this._password = data.password;
        this._newPassword = data.newPassword;
        this._dateCreate = new LocalDate(data.dateCreate);
        this._userDetails = !data.userDetails || data.userDetails.id !== null ? new UserDetails(data.userDetails) : this.createEmptyUserDetails();
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get email(): string {
        return this._email;
    }

    set email(value: string) {
        this._email = value;
    }

    get password(): string {
        return this._password;
    }

    set password(value: string) {
        this._password = value;
    }

    get newPassword(): string {
        return this._newPassword;
    }

    set newPassword(value: string) {
        this._newPassword = value;
    }

    get dateCreate(): LocalDate {
        return this._dateCreate;
    }

    set dateCreate(value: LocalDate) {
        this._dateCreate = value;
    }

    get userDetails(): UserDetails {
        return this._userDetails;
    }

    set userDetails(value: UserDetails) {
        this._userDetails = value;
    }

    createEmptyUserDetails() {
        return new UserDetails({
            id : '',
            firstName: '',
            lastName: '',
            about: '',
            birthday: {
                year: '',
                month: '',
                day: ''
            },
            nick: '',
            photoLink: ''
        });
    }

    toObject() {
        return {
            id: this.id,
            email: this.email,
            password: this.password,
            newPassword: this.newPassword,
            dateCreate: {
                year: this.dateCreate.year,
                month: this.dateCreate.month,
                day: this.dateCreate.day
            },
            userDetails: {
                id: this.userDetails.id,
                firstName: this.userDetails.firstName,
                lastName: this.userDetails.lastName,
                about: this.userDetails.about,
                birthday: {
                    year: this.userDetails.birthday.year,
                    month: this.userDetails.birthday.month,
                    day: this.userDetails.birthday.day
                },
                nick: this.userDetails.nick,
                photoLink: this.userDetails.photoLink
            }
        };
    }

    printUserName() {
        if (this.userDetails.firstName !== null || this.userDetails.lastName !== null) {
            return this.userDetails.firstName + ' ' + this.userDetails.lastName;
        } else {
            return '';
        }
    }

    isEmptyPhotoLink() {
        return this.userDetails.photoLink === null;
    }

    getPhotoLink() {
        return this.userDetails.photoLink;
    }
}
