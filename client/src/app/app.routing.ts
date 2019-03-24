import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AboutComponent } from './components/about/about.component';
import { MusicComponent } from './components/music/music.component';
import { UsersComponent } from './components/users/users.component';
import { SettingsComponent } from './components/settings/settings.component';
import { DownloadComponent } from './components/download/download.component';
import { RegistrationComponent } from './components/registration/registration.component';

const appRoutes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'music', component: MusicComponent },
    { path: 'users', component: UsersComponent },
    { path: 'about', component: AboutComponent },
    { path: 'settings', component: SettingsComponent },
    { path: 'download', component: DownloadComponent },
    { path: 'registration', component: RegistrationComponent },
    { path: '**', redirectTo: 'home' }
];

export const routing = RouterModule.forRoot(appRoutes);
