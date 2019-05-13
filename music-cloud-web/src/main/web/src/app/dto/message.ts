import { LocalDate } from './local-date';

export class Message {
    private _id: number;
    private _text: string;
    private _time: LocalDate;
    private _userId: number;


    constructor(data: any) {
        this._id = data.id;
        this._text = data.text;
        this._time = new LocalDate(data.time);
        this._userId = data.userId;
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get text(): string {
        return this._text;
    }

    set text(value: string) {
        this._text = value;
    }

    get time(): LocalDate {
        return this._time;
    }

    set time(value: LocalDate) {
        this._time = value;
    }

    get userId(): number {
        return this._userId;
    }

    set userId(value: number) {
        this._userId = value;
    }
}
