package services;

import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.TaskDto;
import dtos.TaskUpdateDto;
import enums.Status;
import enums.TaskType;
import exceptions.InvalidTaskException;
import models.Person;
import models.Task;
import utils.Constants;
import utils.MsgGenerator;
import utils.TaskConverter;
import utils.Validators;

import java.util.UUID;

/**
 * A service class for Task which implements the server logic and interaction with DB
 * */
public class TasksService {

    /**
     * Gets a task from the system by its id.
     * @return the requested task, or null if no such task exists
     * */
    public TaskDto getTask(String id) {
        // Check if task is a chore
        Task task = Task.find.byId(UUID.fromString(id));
        if (task != null) {
            return TaskConverter.modelToDto(task);
        }
        // no such task exists
        return null;
    }

    /**
     * Updates a task in the system.
     * @return the updated task details, or null if no task with the given id exists
     * @throws InvalidTaskException if the request isn't valid
     * */
//    public TaskDto update(String id, TaskUpdateDto taskUpdateDto) throws InvalidTaskException {
//        Chore chore = Chore.find.byId(id);
//        if (chore != null) {
//            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.HOMEWORK)) {
//                return updateChore(TaskConverter.updateDtoToHomeworkDto(taskUpdateDto), chore);
//            }
//            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.CHORE) || taskUpdateDto.getType() == null) {
//                return updateChore(TaskConverter.updateDtoToChoreDto(taskUpdateDto), chore);
//            }
//            throw new InvalidTaskException(MsgGenerator.invalidTaskType(taskUpdateDto.getType()));
//        }
//        HomeWork homeWork = HomeWork.find.byId(id);
//        if (homeWork != null) {
//            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.CHORE)) {
//                return updateHomework(TaskConverter.updateDtoToChoreDto(taskUpdateDto), homeWork);
//            }
//            if (taskUpdateDto.getType().equalsIgnoreCase(Constants.HOMEWORK) || taskUpdateDto.getType() == null) {
//                return updateHomework(TaskConverter.updateDtoToHomeworkDto(taskUpdateDto), homeWork);
//            }
//            throw new InvalidTaskException(MsgGenerator.invalidTaskType(taskUpdateDto.getType()));
//        }
//        return null;
//    }

    public TaskDto update(String id, TaskUpdateDto taskUpdateDto) throws InvalidTaskException {
        Task taskToUpdate = Task.find.byId(UUID.fromString(id));
        if (taskToUpdate != null) {
//            if (isValidRequestedTaskType(taskUpdateDto.getType())) {
                if (taskToUpdate.getType() == TaskType.Chore) {
                    return updateChore(taskUpdateDto, taskToUpdate);
                }
                return updateHomework(taskUpdateDto, taskToUpdate);
//            }
//            throw new InvalidTaskException(MsgGenerator.invalidTaskType(taskUpdateDto.getType()));
        }
        return null;
    }

    private boolean isValidRequestedTaskType(String type) {
        if (type == null) {
            return true;
        }
        return type.equalsIgnoreCase(Constants.CHORE) || type.equalsIgnoreCase(Constants.HOMEWORK);
    }

    /**
     * Updates a task's status.
     * @return whether the update was successful
     * */
//    public boolean updateTaskStatus(String id, Status status) {
//        Chore choreToUpdate = Chore.find.byId(id);
//        if (choreToUpdate != null) {
//            choreToUpdate.setStatus(status);
//            choreToUpdate.update();
//            return true;
//        }
//        HomeWork homeWorkToUpdate = HomeWork.find.byId(id);
//        if (homeWorkToUpdate != null) {
//            homeWorkToUpdate.setStatus(status);
//            homeWorkToUpdate.update();
//            return true;
//        }
//        return false;
//    }

    public boolean updateTaskStatus(String id, Status status) {
        Task taskToUpdate = Task.find.byId(UUID.fromString(id));
        if (taskToUpdate != null) {
            taskToUpdate.setStatus(status);
            taskToUpdate.update();
            return true;
        }
        return false;
    }

    /**
     * Updates a task's owner.
     * @return whether the update was successful
     * */
    public boolean updateTaskOwner(String id, String ownerId) throws InvalidTaskException {
        if (ownerId == null) {
            throw new InvalidTaskException(MsgGenerator.missingOwnerId());
        }
        if (!Validators.isValidId(ownerId)) {
            throw new InvalidTaskException(MsgGenerator.personIdNotFound(ownerId));
        }
        Person person = Person.find.byId(ownerId);
        if (person == null) {
            throw new InvalidTaskException(MsgGenerator.personIdNotFound(ownerId));
        }

        Task taskToUpdate = Task.find.byId(UUID.fromString(id));
        if (taskToUpdate != null) {
            taskToUpdate.setOwner(person);
            taskToUpdate.update();
            return true;
        }
        return false;
    }

    /**
     * Deletes a task from the system.
     * @return whether the update was successful
     * */
//    public boolean delete(String id) {
//        Chore choreToDelete = Chore.find.byId(id);
//        if (choreToDelete != null) {
//            return choreToDelete.delete();
//        }
//        HomeWork homeWorkToDelete = HomeWork.find.byId(id);
//        if (homeWorkToDelete != null) {
//            return homeWorkToDelete.delete();
//        }
//        return false;
//    }

    public boolean delete(String id) {
        Task taskToDelete = Task.find.byId(UUID.fromString(id));
        if (taskToDelete != null) {
            return taskToDelete.delete();
        }
        return false;
    }

    //////////////////
    // Helper methods
    //////////////////
    //TODO - verify that this scenario is possible (type change of task via update). if so, what is the expected behavior in case of missing fields?
//    private TaskDto updateChore(HomeWorkDto updateDto, Chore chore) throws InvalidTaskException {
//        Validators.validateAllHomeWorkFieldsPresent(updateDto);
//        HomeWork updatedTask = new HomeWork(
//                chore.getId(),
//                chore.getOwner(),
//                updateDto.getStatus() != null ? updateDto.getStatus() : chore.getStatus(),
//                updateDto.getCourse(),
//                updateDto.getDueDate(),
//                updateDto.getDetails()
//                );
//        updatedTask.save();
//        chore.delete();
//        return TaskConverter.modelToDto(updatedTask);
//    }
//
//    private TaskDto updateChore(ChoreDto updateDto, Chore chore) {
//        chore.setStatus(updateDto.getStatus() == null ? chore.getStatus() : updateDto.getStatus());
//        chore.setDescription(updateDto.getDescription() == null ? chore.getDescription() : updateDto.getDescription());
//        chore.setSize(updateDto.getSize() == null ? chore.getSize() : updateDto.getSize());
//        chore.update();
//        return TaskConverter.modelToDto(chore);
//    }

    private TaskDto updateChore(TaskUpdateDto updateDto, Task taskToUpdate) throws InvalidTaskException {
        taskToUpdate.setStatus(updateDto.getStatus() == null ? taskToUpdate.getStatus() : updateDto.getStatus());
//        if (updateDto.getType() == null || updateDto.getType().equalsIgnoreCase(Constants.CHORE)) {
        if (updateDto.getType() == null || updateDto.getType() == TaskType.Chore) {
            ChoreDto choreDto = TaskConverter.updateDtoToChoreDto(updateDto);
            taskToUpdate.setDescription(choreDto.getDescription() == null ? taskToUpdate.getDescription() : choreDto.getDescription());
            taskToUpdate.setSize(choreDto.getSize() == null ? taskToUpdate.getSize() : choreDto.getSize());
        } else {
            HomeWorkDto homeWorkDto = TaskConverter.updateDtoToHomeworkDto(updateDto);
            Validators.validateAllHomeWorkFieldsPresent(homeWorkDto);
            taskToUpdate.setDescription(null);
            taskToUpdate.setSize(null);
            taskToUpdate.setCourse(homeWorkDto.getCourse());
            taskToUpdate.setDetails(homeWorkDto.getDetails());
            taskToUpdate.setDueDate(homeWorkDto.getDueDate());
            taskToUpdate.setType(TaskType.HomeWork);
        }
        taskToUpdate.save();
        return TaskConverter.modelToDto(taskToUpdate);
    }

//    private TaskDto updateHomework(ChoreDto updateDto, HomeWork homeWork) throws InvalidTaskException {
//        Validators.validateAllChoreFieldsPresent(updateDto);
//        Chore updatedTask = new Chore(
//                homeWork.getId(),
////                homeWork.getOwnerId(),
//                homeWork.getOwner(),
//                updateDto.getStatus() != null ? updateDto.getStatus() : homeWork.getStatus(),
//                updateDto.getDescription(),
//                updateDto.getSize()
//        );
//        updatedTask.save();
//        homeWork.delete();
//        return TaskConverter.modelToDto(updatedTask);
//    }

//    private TaskDto updateHomework(ChoreDto updateDto, Task taskToUpdate) throws InvalidTaskException {
//        Validators.validateAllChoreFieldsPresent(updateDto);
//        if ()
//        updatedTask.save();
//        homeWork.delete();
//        return TaskConverter.modelToDto(updatedTask);
//    }
//
//    private TaskDto updateHomework(HomeWorkDto updateDto, HomeWork homeWork) {
//        homeWork.setStatus(updateDto.getStatus() == null ? homeWork.getStatus() : updateDto.getStatus());
//        homeWork.setCourse(updateDto.getCourse() == null ? homeWork.getCourse() : updateDto.getCourse());
//        homeWork.setDueDate(updateDto.getDueDate() == null ? homeWork.getDueDate() : updateDto.getDueDate());
//        homeWork.setDetails(updateDto.getDetails() == null ? homeWork.getDetails() : updateDto.getDetails());
//        homeWork.update();
//        return TaskConverter.modelToDto(homeWork);
//    }

    private TaskDto updateHomework(TaskUpdateDto updateDto, Task taskToUpdate) throws InvalidTaskException {
        taskToUpdate.setStatus(updateDto.getStatus() == null ? taskToUpdate.getStatus() : updateDto.getStatus());
//        if (updateDto.getType() == null || updateDto.getType().equalsIgnoreCase(Constants.HOMEWORK)) {
        if (updateDto.getType() == null || updateDto.getType() == TaskType.HomeWork) {
            HomeWorkDto homeWorkDto = TaskConverter.updateDtoToHomeworkDto(updateDto);
            taskToUpdate.setCourse(homeWorkDto.getCourse() == null ? taskToUpdate.getCourse(): homeWorkDto.getCourse());
            taskToUpdate.setDetails(homeWorkDto.getDetails() == null ? taskToUpdate.getDetails() : homeWorkDto.getDetails());
            taskToUpdate.setDueDate(homeWorkDto.getDueDate() == null ? taskToUpdate.getDueDate() : homeWorkDto.getDueDate());
        } else {
            ChoreDto choreDto = TaskConverter.updateDtoToChoreDto(updateDto);
            Validators.validateAllChoreFieldsPresent(choreDto);
            taskToUpdate.setDescription(choreDto.getDescription() == null ? taskToUpdate.getDescription() : choreDto.getDescription());
            taskToUpdate.setSize(choreDto.getSize() == null ? taskToUpdate.getSize() : choreDto.getSize());
            taskToUpdate.setCourse(null);
            taskToUpdate.setDetails(null);
            taskToUpdate.setDueDate(null);
            taskToUpdate.setType(TaskType.Chore);
        }
        taskToUpdate.save();
        return TaskConverter.modelToDto(taskToUpdate);
    }
}
