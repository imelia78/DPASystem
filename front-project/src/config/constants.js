export const PROCEDURE_TYPES = [
  {
    category: 'Cardiology',
    subtypes: [
      { id: 'c1', name: 'General Cardiology Consult', duration: 30 },
      { id: 'c2', name: 'Interventional Cardiology', duration: 60 },
      { id: 'c3', name: 'Electrophysiology Eval', duration: 60 }
    ]
  },
  {
    category: 'Neurology',
    subtypes: [
      { id: 'n1', name: 'General Neurology Consult', duration: 45 },
      { id: 'n2', name: 'Comprehensive Neurological Exam', duration: 60 }
    ]
  },
  {
    category: 'Orthopedics',
    subtypes: [
      { id: 'o1', name: 'General Orthopedic Consult', duration: 30 },
      { id: 'o2', name: 'Sports Medicine Eval', duration: 45 },
      { id: 'o3', name: 'Pre-Surgical Assessment (Joint/Spine)', duration: 60 }
    ]
  },
  {
    category: 'General Medicine',
    subtypes: [
      { id: 'g1', name: 'Routine Checkup / Primary Care', duration: 20 },
      { id: 'g2', name: 'Complex Internal Medicine Consult', duration: 45 }
    ]
  },
  {
    category: 'Ophthalmology',
    subtypes: [
      { id: 'op1', name: 'Routine Eye Exam', duration: 20 },
      { id: 'op2', name: 'Comprehensive Eye Exam (with dilation)', duration: 45 }
    ]
  },
  {
    category: 'Dentistry',
    subtypes: [
      { id: 'den1', name: 'General Checkup / Cleaning', duration: 30 },
      { id: 'den2', name: 'Orthodontic Consultation', duration: 45 },
      { id: 'den3', name: 'Oral Surgery Consult', duration: 60 }
    ]
  }
];
