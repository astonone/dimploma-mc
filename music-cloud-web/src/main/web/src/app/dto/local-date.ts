export class LocalDate {
    private _year: number;
    private _month: number;
    private _day: number;

    public static fromDate(date: Date) {
        return new LocalDate({
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: date.getDate(),
            hours: '0',
            minutes: '0',
            seconds: '0'
        });
    }

    public static getObjFromDate(date: Date) {
        return {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: date.getDate(),
            hours: '0',
            minutes: '0',
            seconds: '0'
        };
    }

    constructor(data: any) {
        this._year = data.year;
        this._month = data.month;
        this._day = data.day;
    }

    get year(): number {
        return this._year;
    }

    set year(value: number) {
        this._year = value;
    }

    get month(): number {
        return this._month;
    }

    set month(value: number) {
        this._month = value;
    }

    get day(): number {
        return this._day;
    }

    set day(value: number) {
        this._day = value;
    }

    public printDate() {
        if (this.day !== null && this.month !== null && this.year !== null) {
            return this.day + '.' + this.month + '.' + this.year;
        } else {
            return '';
        }
    }

    public toDate() {
        if (this.day !== null && this.month !== null && this.year !== null) {
            return new Date(this.year, this.month - 1, this.day);
        } else {
            return null;
        }
    }
}
