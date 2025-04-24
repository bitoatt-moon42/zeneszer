import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { InstrumentListComponent } from './instrument/instrument-list.component';
import { InstrumentAddComponent } from './instrument/instrument-add.component';
import { InstrumentEditComponent } from './instrument/instrument-edit.component';
import { UserAddComponent } from './user/user-add.component';
import { ErrorComponent } from './error/error.component';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'instruments/add',
    component: InstrumentAddComponent,
    title: $localize`:@@instrument.add.headline:Add Instrument`
  },
  {
    path: 'instruments/add/:type',
    component: InstrumentAddComponent,
    title: $localize`:@@instrument.add.headline:Add Instrument`
  },
  {
    path: 'instruments/:type',
    component: InstrumentListComponent,
    title: $localize`:@@instrument.list.headline:Instruments`
  },
  {
    path: 'instruments',
    component: InstrumentListComponent,
    title: $localize`:@@instrument.list.headline:Instruments`
  },
  {
    path: 'instruments/edit/:id',
    component: InstrumentEditComponent,
    title: $localize`:@@instrument.edit.headline:Edit Instrument`
  },
  {
    path: 'instruments/edit/:id/:type',
    component: InstrumentEditComponent,
    title: $localize`:@@instrument.edit.headline:Edit Instrument`
  },
  {
    path: 'users/:action',
    component: UserAddComponent,
    title: $localize`:@@user.add.headline:Add User`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.page.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];
