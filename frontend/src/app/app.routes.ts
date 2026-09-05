import { Routes } from '@angular/router';
import { VendorDashboardComponent } from './vendors/vendor-dashboard';
import { VendorPortalComponent } from './vendors/vendor-portal';
import { DataUploadComponent } from './upload/data-upload';

export const routes: Routes = [
  { path: 'vendor-operations', component: VendorDashboardComponent },
  { path: 'vendor-portal', component: VendorPortalComponent },
  { path: 'vendors', component: VendorPortalComponent },
  { path: 'data-upload', component: DataUploadComponent },
  { path: '', redirectTo: 'vendor-operations', pathMatch: 'full' }
];
