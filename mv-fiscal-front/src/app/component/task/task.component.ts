import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgForm, FormsModule } from '@angular/forms';
import { TaskDTO } from '../../model/task.dto';
import { LazyLoadEvent, MessageService } from 'primeng/api';
import { TaskService } from '../../service/task/task.service';
import { DateUtil } from '../../util/date.util';
import { ButtonModule } from 'primeng/button';
import { PaginatorModule } from 'primeng/paginator';
import { SelectModule } from 'primeng/select';
import { UserDTO } from '../../model/user.dto';
import { UserService } from '../../service/user/user.service';

@Component({
  selector: 'app-task',
  imports: [CommonModule, FormsModule, PaginatorModule, SelectModule],
  standalone: true,
  providers: [ButtonModule],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css',
})
export class TaskComponent implements OnInit {
  @ViewChild('taskForm') taskForm!: NgForm;
  public searchMode: boolean = false;
  public tasks: TaskDTO[] = [];
  public totalTasks = 0;
  public currentPage: number = 0;
  public pageSize: number = 5;
  public users: UserDTO[] = [];
  public selectedUserId: number | null = null;
  public userPage = 0;
  public userPageSize = 5;
  public totalUsers = 0;
  public isEditMode = false;

  constructor(
    private taskService: TaskService,
    private userService: UserService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService
      .getPageByFilter(null, '', '', null, this.userPage, this.userPageSize)
      .subscribe({
        next: (response) => {
          if (response && response.responseBody) {
            this.users = response.responseBody.content;
            this.totalUsers = response.responseBody.totalElements;
          }
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro ao buscar usuários',
            detail: error.error.message,
          });
        },
      });
  }

  onUserPageChange(direction: 'prev' | 'next'): void {
    if (direction === 'prev' && this.userPage > 0) {
      this.userPage--;
    } else if (
      (direction === 'next' &&
        (this.userPage + 1) * this.userPageSize < this.totalUsers) ||
      this.userPage === 0
    ) {
      this.userPage++;
    }

    this.loadUsers();
  }

  onPageChange(event: any) {
    this.getPagedTasksByFilter(event.page);
  }

  onSubmit(taskForm: NgForm) {
    this.isEditMode = false;

    if (taskForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Dados inválidos',
        detail: 'Preencha os campos obrigatórios com valores válidos',
      });
      return;
    }

    const task = taskForm.value as TaskDTO;
    this.taskService.save(task).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Tarefa salva',
          detail: `Tarefa ${task.title} salva com sucesso!`,
        });

        this.getPagedTasksByFilter(task.id ? this.currentPage : 0);
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro ao salvar tarefa',
          detail: error.error.message,
        });
      },
    });
  }

  onSearch(taskForm: NgForm): void {
    this.isEditMode = false;

    if (!this.searchMode) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Modo pesquisa desativado',
        detail: `Você não está em modo de pesquisa`,
      });
      return;
    }

    if (this.isFormEmpty(taskForm)) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Nenhum filtro informado',
        detail: `Informe pelo menos um filtro para a pesquisa`,
      });
      return;
    }

    this.getPagedTasksByFilter(0);
    this.searchMode = false;
  }

  isFormEmpty(taskForm: NgForm): boolean {
    const formFields = taskForm.value;
    return (
      !formFields.id &&
      !formFields.title &&
      !formFields.description &&
      !formFields.status &&
      !formFields.createdDate &&
      !formFields.limitDate &&
      !formFields.userId
    );
  }

  onClearForm(): void {
    this.taskForm.resetForm();
    this.tasks = [];
    this.totalTasks = 0;
    this.currentPage = 0;
    this.userPageSize = 5;
    this.totalUsers = 0;
    this.searchMode = false;
    this.isEditMode = false;
  }

  onToggleSearchMode(value: boolean) {
    this.searchMode = value;

    if (!value) {
      this.taskForm?.form.patchValue({
        id: null,
        status: null,
        createdDate: null,
      });
    } else {
      this.isEditMode = false;
    }
  }

  onEdit(task: TaskDTO) {
    this.isEditMode = true;

    this.taskForm.setValue({
      id: task.id,
      title: task.title,
      description: task.description,
      status: task.status,
      createdDate: DateUtil.formatDateForInput(task.createdDate),
      limitDate: DateUtil.formatDateForInput(task.limitDate),
      userId: task.userId,
    });
  }

  onDelete(taskId: number) {
    this.delete(taskId);
  }

  private delete(taskId: number) {
    this.taskService.deleteById(taskId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Tarefa excluída',
          detail: `Tarefa ${taskId} excluída com sucesso!`,
        });

        this.getPagedTasksByFilter(this.currentPage);
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro ao excluir tarefa',
          detail: error.error.message,
        });
      },
    });
  }

  private getPagedTasksByFilter(pageNumber: number) {
    const taskDTO = this.taskForm.value as TaskDTO;
    this.taskService
      .getPageByFilter(
        taskDTO.id,
        taskDTO.title,
        taskDTO.description,
        taskDTO.status,
        taskDTO.createdDate,
        taskDTO.limitDate,
        taskDTO.userId,
        pageNumber,
        this.pageSize
      )
      .subscribe({
        next: (response) => {
          if (response && response.responseBody) {
            this.tasks = response.responseBody.content;
            this.totalTasks = response.responseBody.totalElements;
            this.currentPage = response.responseBody.number;
          } else {
            this.onClearForm();
          }
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro ao carregar tarefas',
            detail: error.error.message,
          });
        },
      });
  }
}
