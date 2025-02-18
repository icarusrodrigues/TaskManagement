import Task from './Task';

const FilteredTasks = (props) => {

    return (<>
        <div className='flex flex-column w-full'>
            <h2 className='p-2'>{props.name}</h2>
            <ul className='flex flex-row flex-wrap list-none justify-content-evenvly p-0'>
                {
                    (props.tasks ?? []).map((task) => (
                        <li className='col-4' key={task.id}>
                            <Task task={task} searchTasks={props.searchTasks}/>
                        </li>    
                    ))
                }
            </ul>
        </div>
    </>);
}

export default FilteredTasks;