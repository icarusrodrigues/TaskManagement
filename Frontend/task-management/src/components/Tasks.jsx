import Task from './Task';

const Tasks = (props) => {

    return (<>
        <div className='flex flex-column w-3'>
            <h2 className='p-2'>{props.name}</h2>
            <ul className='flex flex-column flex-wrap list-none justify-content-between p-0'>
                {
                    (props.tasks ?? []).map((task) => (
                        <li className='w-full mb-2' key={task.id}>
                            <Task task={task} searchTasks={props.searchTasks}/>
                        </li>    
                    ))
                }
            </ul>
        </div>
    </>);
}

export default Tasks;