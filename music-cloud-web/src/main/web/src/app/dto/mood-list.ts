import { Mood } from './mood';

export class MoodList {
    private _allCount: number;
    private _moods: Mood[] = [];

    constructor(data: any) {
        this._allCount = data.allCount;
        data.moods.forEach(mood => {
            this._moods.push(new Mood(mood));
        });
    }

    get allCount(): number {
        return this._allCount;
    }

    set allCount(value: number) {
        this._allCount = value;
    }

    get moods(): Mood[] {
        return this._moods;
    }

    set moods(value: Mood[]) {
        this._moods = value;
    }
}
