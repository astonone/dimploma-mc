import { Playlist } from './playlist';

export class PlaylistList {
    private _playlists: Playlist[] = [];

    constructor(data: any) {
        data.playlistWithTrackDTOS.forEach(playlist => {
            this._playlists.push(new Playlist(playlist));
        });
    }

    get playlists(): Playlist[] {
        return this._playlists;
    }

    set playlists(value: Playlist[]) {
        this._playlists = value;
    }
}
