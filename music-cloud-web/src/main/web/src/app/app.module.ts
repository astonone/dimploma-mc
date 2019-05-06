import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { routing } from './app.routing';

import { MainUiModule } from './modules/main-ui/main-ui.module';

/*Services*/
import { UserService } from './services/user.service';
import { SharedService } from './services/shared.service';
import { TrackService } from './services/track.service';
import { FileService } from './services/file.service';
import { GenreService } from './services/genre.service';
import { MoodService } from './services/mood.service';
import { PlaylistService } from './services/playlist.service';
import { PlayService } from './services/play.service';

/*Components*/
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AboutComponent } from './components/about/about.component';
import { MusicComponent } from './components/music/music.component';
import { UsersComponent } from './components/users/users.component';
import { UploadComponent } from './components/upload/upload.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { SettingsComponent } from './components/settings/settings.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

/*Popups*/
import { CreateUserDialog } from './components/registration/dialog/create-user-dialog';
import { ErrorCreateUserDialog } from './components/registration/dialog/error-create-user-dialog';
import { AddTrackToUserDialog } from './components/music/dialog/add-track-to-user-dialog';
import { DeleteTrackDialog } from './components/music/dialog/delete-track-dialog';
import { ChangeTrackDialog } from './components/music/dialog/change-track-dialog';
import { InfoDialog } from './components/home/dialog/info-dialog';
import { AdditionalComponent } from './components/additional/additional.component';
import { AudioPlayerComponent } from './components/audio-player/audio-player.component';
import { AudioPlayerProxyComponent } from './components/audio-player-proxy/audio-player-proxy.component';
import { CreatePlaylist } from './components/home/dialog/create-playlist';
import { ChangePlaylist } from './components/home/dialog/change-playlist';
import { RemovePlaylist } from './components/home/dialog/remove-playlist';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        routing,
        BrowserAnimationsModule,
        MainUiModule
    ],
    declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent,
        AboutComponent,
        MusicComponent,
        UsersComponent,
        UploadComponent,
        RegistrationComponent,
        SettingsComponent,
        CreateUserDialog,
        ErrorCreateUserDialog,
        UserProfileComponent,
        AddTrackToUserDialog,
        DeleteTrackDialog,
        ChangeTrackDialog,
        InfoDialog,
        AdditionalComponent,
        AudioPlayerComponent,
        AudioPlayerProxyComponent,
        CreatePlaylist,
        ChangePlaylist,
        RemovePlaylist
    ],
    entryComponents: [
        CreateUserDialog,
        ErrorCreateUserDialog,
        AddTrackToUserDialog,
        DeleteTrackDialog,
        ChangeTrackDialog,
        InfoDialog,
        CreatePlaylist,
        ChangePlaylist,
        RemovePlaylist
    ],
    providers: [
        UserService,
        TrackService,
        SharedService,
        FileService,
        GenreService,
        MoodService,
        PlaylistService,
        PlayService,
        AudioPlayerComponent,
        HomeComponent,
        MusicComponent,
        ChangePlaylist
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }
