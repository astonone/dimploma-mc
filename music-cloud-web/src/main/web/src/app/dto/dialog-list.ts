import { Dialog } from './dialog';

export class DialogList {
    private _countAll: number;
    private _dialogs: Dialog[] = [];

    constructor(data: any) {
        this._countAll = data.countAll;
        data.dialogs.forEach(dialog => {
            this._dialogs.push(new Dialog(dialog));
        });
    }

    get countAll(): number {
        return this._countAll;
    }

    set countAll(value: number) {
        this._countAll = value;
    }

    get dialogs(): Dialog[] {
        return this._dialogs;
    }

    set dialogs(value: Dialog[]) {
        this._dialogs = value;
    }
}
