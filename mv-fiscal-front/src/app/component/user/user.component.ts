import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgForm, FormsModule } from '@angular/forms';
import { UserDTO } from '../../model/user.dto';
import { MessageService } from 'primeng/api';
import { UserService } from '../../service/user/user.service';
import { DateUtil } from '../../util/date.util';
import { ButtonModule } from 'primeng/button';
import { PaginatorModule } from 'primeng/paginator';

@Component({
  selector: 'app-user',
  imports: [CommonModule, FormsModule, PaginatorModule],
  standalone: true,
  providers: [ButtonModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
})
export class UserComponent implements OnInit {
  @ViewChild('userForm') userForm!: NgForm;
  public searchMode: boolean = false;
  public users: UserDTO[] = [];
  public totalUsers = 0;
  public currentPage: number = 0;
  public pageSize: number = 5;

  constructor(
    private userService: UserService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {}

  onPageChange(event: any) {
    this.getPagedUsersByFilter(event.page);
  }

  onSubmit(userForm: NgForm) {
    if (userForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Dados inválidos',
        detail: 'Preencha os campos obrigatórios com valores válidos',
      });
      return;
    }

    const user = userForm.value as UserDTO;
    this.userService.save(user).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Usuário salvo',
          detail: `Usuário ${user.name} salvo com sucesso!`,
        });

        this.getPagedUsersByFilter(user.id ? this.currentPage : 0);
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro ao salvar usuário',
          detail: error.error.message.includes('ConstraintViolationException')
            ? 'Já existe um usuário com esse email'
            : error.error.message,
        });
      },
    });
  }

  onSearch(userForm: NgForm): void {
    if (!this.searchMode) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Modo pesquisa desativado',
        detail: `Você não está em modo de pesquisa`,
      });
      return;
    }

    if (this.isFormEmpty(userForm)) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Nenhum filtro informado',
        detail: `Informe pelo menos um filtro para a pesquisa`,
      });
      return;
    }

    this.getPagedUsersByFilter(0);
    this.searchMode = false;
  }

  isFormEmpty(userForm: NgForm): boolean {
    const formFields = userForm.value;
    return (
      !formFields.id &&
      !formFields.name &&
      !formFields.email &&
      !formFields.createdDate
    );
  }

  onClearForm(): void {
    this.userForm.resetForm();
    this.users = [];
    this.totalUsers = 0;
    this.currentPage = 0;
    this.searchMode = false;
  }

  onToggleSearchMode(value: boolean) {
    this.searchMode = value;

    if (!value) {
      this.userForm?.form.patchValue({
        id: null,
        createdDate: null,
      });
    }
  }

  onEdit(user: UserDTO) {
    this.userForm.setValue({
      id: user.id,
      name: user.name,
      email: user.email,
      createdDate: DateUtil.formatDateForInput(user.createdDate),
    });
  }

  onDelete(userId: number) {
    this.delete(userId);
  }

  private delete(userId: number) {
    this.userService.deleteById(userId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Usuário excluído',
          detail: `Usuário ${userId} excluído com sucesso!`,
        });

        this.getPagedUsersByFilter(this.currentPage);
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro ao excluir usuário',
          detail: error.error.message,
        });
      },
    });
  }

  private getPagedUsersByFilter(pageNumber: number) {
    const userDTO = this.userForm.value as UserDTO;
    this.userService
      .getPageByFilter(
        userDTO.id,
        userDTO.name,
        userDTO.email,
        userDTO.createdDate,
        pageNumber,
        this.pageSize
      )
      .subscribe({
        next: (response) => {
          if (response && response.responseBody) {
            this.users = response.responseBody.content;
            this.totalUsers = response.responseBody.totalElements;
            this.currentPage = response.responseBody.number;
          } else {
            this.onClearForm();
          }
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro ao carregar usuários',
            detail: error.error.message,
          });
        },
      });
  }
}
