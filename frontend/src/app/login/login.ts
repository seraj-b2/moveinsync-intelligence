import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  private readonly router = inject(Router);

  activeRole: 'employee' | 'manager' = 'employee';

  // Form fields
  companyName = 'catalyst-Sac';
  employeeId = 'STW-484475';
  employeePin = '••••••';

  managerId = 'MGR-103';
  managerPassword = '••••••••';

  rememberMe = true;
  showPassword = false;
  isAuthenticating = false;
  errorMessage = '';

  setRole(role: 'employee' | 'manager'): void {
    this.activeRole = role;
    this.errorMessage = '';
  }

  login(): void {
    this.errorMessage = '';

    if (this.activeRole === 'employee') {
      if (!this.companyName.trim()) {
        this.errorMessage = 'Please enter your company identifier.';
        return;
      }
      if (!this.employeeId.trim()) {
        this.errorMessage = 'Please enter your Employee STW ID.';
        return;
      }

      this.isAuthenticating = true;
      setTimeout(() => {
        this.isAuthenticating = false;
        const empId = this.employeeId.trim().toUpperCase();
        const comp = this.companyName.trim();
        this.router.navigate(['/employee', comp, empId]);
      }, 500);
    } else {
      if (!this.companyName.trim()) {
        this.errorMessage = 'Please enter your company identifier.';
        return;
      }
      if (!this.managerId.trim()) {
        this.errorMessage = 'Please enter your Manager ID.';
        return;
      }

      this.isAuthenticating = true;
      setTimeout(() => {
        this.isAuthenticating = false;
        const mgrId = this.managerId.trim().toUpperCase();
        const comp = this.companyName.trim();
        this.router.navigate(['/manager', comp, mgrId]);
      }, 500);
    }
  }
}
