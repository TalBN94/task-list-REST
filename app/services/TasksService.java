package services;

import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.TaskDto;
import dtos.TaskUpdateDto;
import enums.Status;
import exceptions.InvalidTaskException;
import models.Chore;
import models.HomeWork;
import models.Person;
import utils.Constants;
import utils.MsgGenerator;
import utils.TaskConverter;
import utils.Validators;

import java.util.UUID;

public class TasksService {
    public TaskDto getTask(String id) {
        Chore chore = Chore.find.byId(id);
        if (chore != null) {
            return TaskConverter.modelToDto(chore);
        }
        HomeWork homeWork = HomeWork.find.byId(id);
        if (homeWork != null) {
            return TaskConverter.modelToDto(homeWork);
        }
        return null;
    }

    public TaskDto update(String id, TaskUpdateDto taskUpdateDto) throws InvalidTaskException {
        Chore chore = Chore.find.byId(id);
        if (chore != null) {
            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.HOMEWORK)) {
                return updateChore(TaskConverter.updateDtoToHomeworkDto(taskUpdateDto), chore);
            }
            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.CHORE) || taskUpdateDto.getType() == null) {
                return updateChore(TaskConverter.updateDtoToChoreDto(taskUpdateDto), chore);
            }
            throw new InvalidTaskException(MsgGenerator.invalidTaskType(taskUpdateDto.getType()));
        }
        HomeWork homeWork = HomeWork.find.byId(id);
        if (homeWork != null) {
            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.CHORE)) {
                return updateHomework(TaskConverter.updateDtoToChoreDto(taskUpdateDto), homeWork);
            }
            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.HOMEWORK) || taskUpdateDto.getType() == null) {
                return updateHomework(TaskConverter.updateDtoToHomeworkDto(taskUpdateDto), homeWork);
            }
            throw new InvalidTaskException(MsgGenerator.invalidTaskType(taskUpdateDto.getType()));
        }
        return null;
    }

    public boolean updateTaskStatus(String id, Status status) {
        Chore choreToUpdate = Chore.find.byId(id);
        if (choreToUpdate != null) {
            choreToUpdate.setStatus(status);
            choreToUpdate.update();
            return true;
        }
        HomeWork homeWorkToUpdate = HomeWork.find.byId(id);
        if (homeWorkToUpdate != null) {
            homeWorkToUpdate.setStatus(status);
            homeWorkToUpdate.update();
            return true;
        }
        return false;
    }

    public boolean updateTaskOwner(String id, String ownerId) throws InvalidTaskException {
        if (Person.find.byId(ownerId) == null) {
            throw new InvalidTaskException(MsgGenerator.personIdNotFound(ownerId));
        }

        Chore choreToUpdate = Chore.find.byId(id);
        if (choreToUpdate != null) {
            choreToUpdate.setOwnerId(UUID.fromString(ownerId));
            choreToUpdate.update();
            return true;
        }
        HomeWork homeWorkToUpdate = HomeWork.find.byId(id);
        if (homeWorkToUpdate != null) {
            homeWorkToUpdate.setOwnerId(UUID.fromString(ownerId));
            homeWorkToUpdate.update();
            return true;
        }
        return false;
    }
    public boolean delete(String id) {
        Chore choreToDelete = Chore.find.byId(id);
        if (choreToDelete != null) {
            return choreToDelete.delete();
        }
        HomeWork homeWorkToDelete = HomeWork.find.byId(id);
        if (homeWorkToDelete != null) {
            return homeWorkToDelete.delete();
        }
        return false;
    }

    //TODO - verify that this scenario is possible (type change of task via update). if so, what is the expected behavior in case of missing fields?

    private TaskDto updateChore(HomeWorkDto updateDto, Chore chore) throws InvalidTaskException {
        Validators.validateAllHomeWorkFieldsPresent(updateDto);
        HomeWork updatedTask = new HomeWork(
                chore.getId(),
                chore.getOwnerId(),
                updateDto.getStatus() != null ? updateDto.getStatus() : chore.getStatus(),
                updateDto.getCourse(),
                updateDto.getDueDate(),
                updateDto.getDetails()
                );
        updatedTask.save();
        chore.delete();
        return TaskConverter.modelToDto(updatedTask);
    }

    private TaskDto updateChore(ChoreDto updateDto, Chore chore) {
        chore.setStatus(updateDto.getStatus() == null ? chore.getStatus() : updateDto.getStatus());
        chore.setDescription(updateDto.getDescription() == null ? chore.getDescription() : updateDto.getDescription());
        chore.setSize(updateDto.getSize() == null ? chore.getSize() : updateDto.getSize());
        chore.update();
        return TaskConverter.modelToDto(chore);
    }

    private TaskDto updateHomework(ChoreDto updateDto, HomeWork homeWork) throws InvalidTaskException {
        Validators.validateAllChoreFieldsPresent(updateDto);
        Chore updatedTask = new Chore(
                homeWork.getId(),
                homeWork.getOwnerId(),
                updateDto.getStatus() != null ? updateDto.getStatus() : homeWork.getStatus(),
                updateDto.getDescription(),
                updateDto.getSize()
        );
        updatedTask.save();
        homeWork.delete();
        return TaskConverter.modelToDto(updatedTask);
    }

    private TaskDto updateHomework(HomeWorkDto updateDto, HomeWork homeWork) {
        homeWork.setStatus(updateDto.getStatus() == null ? homeWork.getStatus() : updateDto.getStatus());
        homeWork.setCourse(updateDto.getCourse() == null ? homeWork.getCourse() : updateDto.getCourse());
        homeWork.setDueDate(updateDto.getDueDate() == null ? homeWork.getDueDate() : updateDto.getDueDate());
        homeWork.setDetails(updateDto.getDetails() == null ? homeWork.getDetails() : updateDto.getDetails());
        homeWork.update();
        return TaskConverter.modelToDto(homeWork);
    }
}
